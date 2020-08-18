package gin.util;

import gin.Patch;
import gin.test.UnitTest;
import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gin.fitness.DistanceMetric.getDistanceUnknownType;


public class GPNovelFix extends GPSimple {

    @Argument(alias = "rec", description = "Record all fitness values in a HashMap")
    protected Boolean record = false;

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPNovelFix(String[] args) {
        super(args);
        Args.parseOrExit(this, args);
        printAdditionalArguments();
    }

    private void printAdditionalArguments() {
        Logger.info("Record all fitness values in a HashMap: " + record);
    }

    private Map<Patch, Double> recordedFitness = new HashMap<>();

    /*============== Implementation of abstract methods  ==============*/

    @Override
    protected UnitTestResultSet initFitness(String className, List<UnitTest> tests, Patch origPatch) {
        this.recordedFitness = new HashMap<>();
        return testPatch(className, tests, origPatch);
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

    // fitness calculation process
    private double calculate(UnitTestResultSet results) {
        int numOfFailedTests = 0;
        double score = 0;

        List<UnitTestResult> failedTests = new ArrayList<>();

        // count number of passed and failed tests
        for (UnitTestResult result : results.getResults()) {
            if (!result.getPassed()) {
                numOfFailedTests++;
                failedTests.add(result);
            }
        }

        // sum up distance of all
        for (UnitTestResult result : failedTests) {
            String exceptionType = result.getExceptionType();
            double nor_dis = 0;
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR) || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                double distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue(), false);
                nor_dis = normalize(distance, 1) / numOfFailedTests;
            } else {
                nor_dis = normalize(Integer.MAX_VALUE, 1) / numOfFailedTests;
            }
            score += nor_dis;
        }

        score += numOfFailedTests;

        // the goal is to maximum the fitness
        score = results.getResults().size() + 1 - score;

        return score;
    }

    public double normalize(double x, double alpha) {
        return x / (x + alpha);
    }

}
