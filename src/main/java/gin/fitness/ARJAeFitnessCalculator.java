package gin.fitness;

import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;

import java.util.HashSet;
import java.util.Set;

import static gin.fitness.DistanceMetric.getDistanceUnknownType;

public class ARJAeFitnessCalculator {

    private static Set<String> positiveTestCases;
    private static Set<String> negativeTestCases;

    public static final int MAX_INT = Integer.MAX_VALUE;

    private static final double WEIGHT = 0.2;

    private static void initTestCasesSets(UnitTestResultSet results) {
        positiveTestCases = new HashSet<>();
        negativeTestCases = new HashSet<>();
        for (UnitTestResult result : results.getResults()) {
            String testName = result.getTest().getTestName();
            if (result.getPassed()) {
                positiveTestCases.add(testName);
            } else {
                negativeTestCases.add(testName);
            }
        }
    }

    public static double calculate(UnitTestResultSet results) {
        if (positiveTestCases == null) {
            initTestCasesSets(results);
        }
        double score = 0;
        for (UnitTestResult result : results.getResults()) {
            String exceptionType = result.getExceptionType();
            double distance = 0;
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR) || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue(), false);
            } else {
                distance = MAX_INT;
            }
            String testName = result.getTest().getTestName();
            if (negativeTestCases.contains(testName)) {
                distance *= WEIGHT;
            }
            score += distance;
        }
        return score;
    }
}
