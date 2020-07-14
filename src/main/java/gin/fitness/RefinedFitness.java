package gin.fitness;

import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class RefinedFitness extends Fitness implements Comparable<RefinedFitness> {

    public static final int MAX_INT = Integer.MAX_VALUE;

    private int numOfPassedTests = 0;
    private int numOfFailedTests = 0;

    private double sumOfDistance = 0;

    // Constructor
    public RefinedFitness(UnitTestResultSet results) {

        List<UnitTestResult> failedTests = new ArrayList<>();

        // count number of passed and failed tests
        for (UnitTestResult result : results.getResults()) {
            if (result.getPassed()) {
                numOfPassedTests++;
            } else {
                numOfFailedTests++;
                failedTests.add(result);
            }
        }

        // sum up distance of all
        for (UnitTestResult result : failedTests) {
            String exceptionType = result.getExceptionType();
            if (exceptionType.equals(UnitTestResult.TIMEOUT_EXCEPTION)) {
                sumOfDistance += MAX_INT;
            } else if (exceptionType.equals(UnitTestResult.ASSERTION_ERROR)
                    || exceptionType.equals(UnitTestResult.COMPARISON_FAILURE)) {
                double distance = getDistanceUnknownType(result.getAssertionExpectedValue(), result.getAssertionActualValue());
                sumOfDistance += distance;
            } else {
                // TODO: ? stack overflow, etc
                sumOfDistance += (double) MAX_INT * 2;
            }
        }
    }

    // TODO: add more assertion type: double, String, array...
    private double getDistanceUnknownType(String expectStr, String actualStr) {
        AssertionValueType assertionValueType = AssertionValueTypeChecker.checkType(expectStr);
        if (assertionValueType == AssertionValueType.BOOLEAN) {
            return 1;
        } else if (assertionValueType == AssertionValueType.DOUBLE) {
            return getDistance(Double.parseDouble(expectStr), Double.parseDouble(actualStr));
        } else if (assertionValueType == AssertionValueType.STRING) {
            return getDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.ARRAY) {
            // TODO: support array distance
        } else {
            System.out.println("else");
        }
        return 0;
    }


    private double getDistance(double expect, double actual) {
        return Math.abs(expect - actual);
    }

    /**
     * compute distance between two Strings using levenshtein distance:
     * minimum number of single-character edits (i.e. insertions, deletions, or substitutions)
     * @param expect expected assertion String
     * @param actual actual assertion String
     * @return the distance between two Strings
     */
    private double getDistance(String expect, String actual) {
        return new LevenshteinDistance().apply(expect, actual);
    }

    private double normalize(double x, double alpha) {
        if (alpha == -1)
            return x; // no normalization
        else
            return x / (x + alpha);
    }

    @Override
    public int compareTo(RefinedFitness o) {
        if (this.numOfPassedTests == o.numOfPassedTests) {
            // 2. smaller distance is better
            double gap = o.sumOfDistance - this.sumOfDistance;
            if (gap < 0)
                return -1;
            else if (gap > 0)
                return 1;
            else
                return 0;
        } else {
            // 1. pass more tests is better
            return this.numOfPassedTests - o.numOfPassedTests;
        }
    }

    @Override
    // TODO: implement
    public String toString() {
        return numOfPassedTests + " ; " + String.format("%5.5f", sumOfDistance);
    }


}
