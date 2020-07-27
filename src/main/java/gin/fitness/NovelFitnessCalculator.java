package gin.fitness;

import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;


import java.util.ArrayList;
import java.util.List;

import static gin.fitness.DistanceMetric.getDistanceUnknownType;

public class NovelFitnessCalculator {

    public static final int MAX_INT = Integer.MAX_VALUE;

    public static double calculate(UnitTestResultSet results) {
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
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR) || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                double distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue(), false);
                score += distance;
            } else {
                score += MAX_INT;
            }
        }

        // normalize score within [0,1]
        score = normalize(score, 1);

        score += numOfFailedTests;

        return score;
    }

    public static double normalize(double x, double alpha) {
        return x / (x + alpha);
    }


}
