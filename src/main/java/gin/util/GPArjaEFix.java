package gin.util;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import gin.Patch;
import gin.test.UnitTest;
import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;
import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gin.fitness.DistanceMetric.getDistanceUnknownType;
import static gin.fitness.DistanceMetric.normalize;

public class GPArjaEFix extends GPFix {

    @Argument(alias = "rec", description = "Record all fitness values in a HashMap")
    protected Boolean record = false;

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPArjaEFix(String[] args) {
        super(args);
        Args.parseOrExit(this, args);
        printAdditionalArguments();
    }

    private void printAdditionalArguments() {
        Logger.info("Record all fitness values in a HashMap: " + record);
    }

    private Map<Patch, Double> recordedFitness = new HashMap<>();

    private Map<UnitTest, Boolean> testResults = new HashMap<>();

    private int passing = 0;

    private int failing = 0;

    private static final double WEIGHT = 0.2;

    /*============== Implementation of abstract methods  ==============*/

    @Override
    protected UnitTestResultSet initFitness(String className, List<UnitTest> tests, Patch origPatch) {
        super.reps = 1;
        Logger.debug("Reset reps, each test to be run only once for fitness calculation.");

        UnitTestResultSet results = testPatch(className, tests, origPatch);
        setup(results);
        return results;
    }

    // Calculate fitness
    @Override
    protected double fitness(UnitTestResultSet results) {
        double fitness = 0;
        Patch patch = results.getPatch();

        if (recordedFitness.containsKey(patch)) {
            return recordedFitness.get(patch);
        }

        if (!results.getCleanCompile()) {
            if (record) {
                recordedFitness.put(patch, fitness);
            }
            return fitness;
        }

        fitness = calculate(results);

        if (record) {
            recordedFitness.put(patch, fitness);
        }
        return fitness;
    }

    @Override
    protected boolean fitnessThreshold(UnitTestResultSet results, double orig) {
        return fitness(results) > 0;
    }

    @Override
    protected double compareFitness(double newFitness, double oldFitness) {
        return newFitness - oldFitness;
    }

    /*============== Helper method  ==============*/

    // Set multiplier and test data for fitness calculations
    private void setup(UnitTestResultSet results) {
        testResults = new HashMap<>();
        recordedFitness = new HashMap<>();

        for (UnitTestResult testResult : results.getResults()) {
            if (testResult.getPassed()) {
                testResults.put(testResult.getTest(), true);
                passing += 1;
            } else {
                testResults.put(testResult.getTest(), false);
                failing += 1;
            }
        }
        Logger.info("Currently failing tests: " + failing);
        Logger.info("Currently passing tests (i.e., current fitness): " + passing);
    }

    private double calculate(UnitTestResultSet results) {
        double posScore = 0, negScore = 0;
        for (UnitTestResult result : results.getResults()) {
            String exceptionType = result.getExceptionType();
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR)
                    || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                double normalizedDistance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue());
                if (testResults.get(result.getTest())) {
                    posScore += normalizedDistance;
                } else {
                    negScore += normalizedDistance;
                }
            } else {
                if (testResults.get(result.getTest())) {
                    posScore += Integer.MAX_VALUE;
                } else {
                    negScore += Integer.MAX_VALUE;
                }
            }
        }

        double score = posScore / passing + WEIGHT * (negScore / failing);

        // the goal is to maximise the fitness score
        return 1 - normalize(score);
    }

}
