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
        int passing = 0;
        int failing = 0;
        this.testResults = new HashMap<>();
        this.recordedFitness = new HashMap<>();

        for (UnitTestResult testResult : results.getResults()) {
            if (testResult.getPassed()) {
                this.testResults.put(testResult.getTest(), true);
                passing += 1;
            } else {
                this.testResults.put(testResult.getTest(), false);
                failing += 1;
            }
        }
        Logger.info("Currently failing tests: " + failing);
        Logger.info("Currently passing tests (i.e., current fitness): " + passing);
        Logger.info("Fitness range: [0, 1]");
    }

    private double calculate(UnitTestResultSet results) {
        double score = 0;
        for (UnitTestResult result : results.getResults()) {
            String exceptionType = result.getExceptionType();
            double distance = 0;
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR) || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue(), false);
            } else {
                distance = Integer.MAX_VALUE;
            }
            String testName = result.getTest().getTestName();
            if (!testResults.get(result.getTest())) {
                distance *= WEIGHT;
            }
            score += distance;
        }

        // the goal is to maximum the fitness score
        score = 1 - normalize(score, 1);

        return score;
    }

    private double normalize(double x, double alpha) {
        return x / (x + alpha);
    }

}
