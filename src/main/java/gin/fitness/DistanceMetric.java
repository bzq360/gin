package gin.fitness;

import com.google.gson.Gson;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.pmw.tinylog.Logger;

public class DistanceMetric {

    enum ARRAY_DISTANCE_TYPE {LEVENSHTEIN, FOR_EACH}

    public static final ARRAY_DISTANCE_TYPE arrDisType = ARRAY_DISTANCE_TYPE.LEVENSHTEIN;

    public static final int MAX_INT = Integer.MAX_VALUE;
    public static final double MAX_DOUBLE = Double.MAX_VALUE;

    /**
     * compute the distance between expected and actual value
     *
     * @param expectStr expected value string
     * @param actualStr actual value string
     * @return distance within [0,1] if normalized, otherwise return distance within [0,MAX_INT]
     */
    public static double getDistanceUnknownType(String expectStr, String actualStr) {
        AssertionValueType assertionValueType = AssertionValueTypeChecker.checkType(expectStr);

        double rawDistance = -1;
        if (assertionValueType == AssertionValueType.BOOLEAN) {
            rawDistance = MAX_INT;
        } else if (assertionValueType == AssertionValueType.INT) {
            rawDistance = getIntDistance(Integer.parseInt(expectStr), Integer.parseInt(actualStr));
        } else if (assertionValueType == AssertionValueType.DOUBLE) {
            double doubleDistance = getDoubleDistance(Double.parseDouble(expectStr), Double.parseDouble(actualStr));
            rawDistance = doubleDistance / MAX_DOUBLE * MAX_INT;
        } else if (assertionValueType == AssertionValueType.STRING) {
            rawDistance = getStringDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.INT_ARRAY) {
            rawDistance = getIntArrayDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.DOUBLE_ARRAY) {
            rawDistance = getDoubleArrayDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.STRING_ARRAY) {
            rawDistance = getStringArrayDistance(expectStr, actualStr);
        } else {
            Logger.error("assertion type not supported.");
        }

        return normalize(rawDistance);
    }

    /**
     * compute the maximum penalty distance
     * @return the maximum penalty distance
     */
    public static double getMaxPenaltyDistance() {
        return normalize(MAX_INT);
    }

    /**
     * normalize the double data into the range of [0, 1)
     *
     * @param x the double data to be normalized
     * @return the normalized data
     */
    public static double normalize(double x) {
        return x / (x + 1);
    }

    /**
     * compute the distance between the expected and actual assertion integers
     *
     * @param expect the expected assertion integer
     * @param actual the actual assertion integer
     * @return the integer distance between the expected and actual assertion integers
     */
    public static int getIntDistance(int expect, int actual) {
        return Math.abs(expect - actual);
    }

    /**
     * compute the distance between the expected and actual assertion doubles
     *
     * @param expect the expected assertion double
     * @param actual the actual assertion double
     * @return the integer distance between the expected and actual assertion doubles
     */
    public static double getDoubleDistance(double expect, double actual) {
        return Math.abs(expect - actual);
    }

    /**
     * compute distance between two Strings using levenshtein distance: minimum number of single-character edits (i.e.
     * insertions, deletions, or substitutions)
     *
     * @param expect expected assertion String
     * @param actual actual assertion String
     * @return the distance between two Strings
     */
    public static int getStringDistance(String expect, String actual) {
        return new LevenshteinDistance().apply(expect, actual);
    }

    /**
     * compute distance between two int arrays
     *
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return integer array distance
     */
    public static double getIntArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return getStringDistance(expectArrStr, actualArrStr);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            Gson gson = new Gson();
            int[] expect = gson.fromJson(expectArrStr, int[].class);
            int[] actual = gson.fromJson(actualArrStr, int[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);

            if (shorter == 0) {
                return longer - shorter;
            }

            double sum = 0;
            for (int i = 0; i < shorter; i++) {
                sum += normalize(getIntDistance(expect[i], actual[i]));
            }
            return (longer - shorter) + sum / shorter;
        }

        return -1;
    }

    /**
     * compute distance between two double arrays
     *
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return double array distance
     */
    public static double getDoubleArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return getStringDistance(expectArrStr, actualArrStr);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            Gson gson = new Gson();
            double[] expect = gson.fromJson(expectArrStr, double[].class);
            double[] actual = gson.fromJson(actualArrStr, double[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);

            if (shorter == 0) {
                return longer - shorter;
            }

            double sum = 0;
            for (int i = 0; i < shorter; i++) {
                sum += normalize(getDoubleDistance(expect[i], actual[i]));
            }
            return (longer - shorter) + sum / shorter;
        }

        return -1;
    }

    /**
     * compute distance between two string arrays
     *
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return string array distance
     */
    public static double getStringArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return getStringDistance(expectArrStr, actualArrStr);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            Gson gson = new Gson();
            String[] expect = gson.fromJson(expectArrStr, String[].class);
            String[] actual = gson.fromJson(actualArrStr, String[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);

            if (shorter == 0) {
                return longer - shorter;
            }

            double sum = 0;
            for (int i = 0; i < shorter; i++) {
                sum += normalize(getStringDistance(expect[i], actual[i]));
            }
            return (longer - shorter) + sum / shorter;
        }

        return -1;
    }


}
