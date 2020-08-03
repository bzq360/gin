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
            double nor_dis = 0;
            if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR) || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                double distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue(), false);
                nor_dis = normalize(distance, 1) / numOfFailedTests;
            } else {
                nor_dis = normalize(MAX_INT, 1) / numOfFailedTests;
            }
            score += nor_dis;
        }

        score += numOfFailedTests;

        return score;
    }

    public static double normalize(double x, double alpha) {
        return x / (x + alpha);
    }


}
