package gin.fitness;

import com.google.gson.Gson;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.pmw.tinylog.Logger;

public class DistanceMetric {

    enum ARRAY_DISTANCE_TYPE {LEVENSHTEIN, FOR_EACH}

    public static final ARRAY_DISTANCE_TYPE arrDisType = ARRAY_DISTANCE_TYPE.LEVENSHTEIN;

    public static final double ALPHA = 1;
    public static final int MAX_INT = Integer.MAX_VALUE;
    public static final double MAX_DOUBLE = Double.MAX_VALUE;

    /**
     * compute the distance between expected and actual value
     *
     * @param expectStr expected value string
     * @param actualStr actual value string
     * @param normalize if normalize distance in [0,1] or not
     * @return distance within [0,1] if normalized, otherwise return distance within [0,MAX_INT]
     */
    public static double getDistanceUnknownType(String expectStr, String actualStr, boolean normalize) {
        AssertionValueType assertionValueType = AssertionValueTypeChecker.checkType(expectStr);

        if (assertionValueType == AssertionValueType.BOOLEAN) {
            if (normalize) return normalize(MAX_INT, ALPHA);
            else return MAX_INT;
        } else if (assertionValueType == AssertionValueType.INT) {
            int distance = getIntDistance(Integer.parseInt(expectStr), Integer.parseInt(actualStr));
            if (normalize) return normalize(distance, ALPHA);
            else return distance;
        } else if (assertionValueType == AssertionValueType.DOUBLE) {
            double distance = getDoubleDistance(Double.parseDouble(expectStr), Double.parseDouble(actualStr));
            distance = distance / MAX_DOUBLE * MAX_INT;
            if (normalize) return normalize(distance, ALPHA);
            else return distance;
        } else if (assertionValueType == AssertionValueType.STRING) {
            int distance = getStringDistance(expectStr, actualStr);
            if (normalize) return normalize(distance, ALPHA);
            else return distance;
        } else if (assertionValueType == AssertionValueType.INT_ARRAY) {
            return getIntArrayDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.DOUBLE_ARRAY) {
            return getDoubleArrayDistance(expectStr, actualStr);
        } else if (assertionValueType == AssertionValueType.STRING_ARRAY) {
            return getStringArrayDistance(expectStr, actualStr);
        }

        Logger.error("assertion type not supported.");
        return -1;
    }

    public static double normalize(double x, double alpha) {
        return x / (x + alpha);
    }

    public static int getIntDistance(int expect, int actual) {
        return Math.abs(expect - actual);
    }

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
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return array distance between [0,1]
     */
    public static double getIntArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return normalize(getStringDistance(expectArrStr, actualArrStr), ALPHA);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            double distance = 0;
            Gson gson = new Gson();
            int[] expect = gson.fromJson(expectArrStr, int[].class);
            int[] actual = gson.fromJson(actualArrStr, int[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);
            for (int i = 0; i < shorter; i++) {
                distance += normalize(getIntDistance(expect[i], actual[i]), ALPHA);
            }
            distance += (longer - shorter);
            return longer == 0 ? 0 : distance / longer;
        }

        return -1;
    }

    /**
     * compute distance between two double arrays
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return array distance between [0,1]
     */
    public static double getDoubleArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return getStringDistance(expectArrStr, actualArrStr);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            double distance = 0;
            Gson gson = new Gson();
            double[] expect = gson.fromJson(expectArrStr, double[].class);
            double[] actual = gson.fromJson(actualArrStr, double[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);
            for (int i = 0; i < shorter; i++) {
                distance += normalize(getDoubleDistance(expect[i], actual[i]), ALPHA);
            }
            distance += (longer - shorter);
            return longer == 0 ? 0 : distance / longer;
        }

        return -1;
    }

    /**
     * compute distance between two string arrays
     * @param expectArrStr expected string of assertion array
     * @param actualArrStr actual string of assertion array
     * @return array distance between [0,1]
     */
    public static double getStringArrayDistance(String expectArrStr, String actualArrStr) {
        if (arrDisType == ARRAY_DISTANCE_TYPE.LEVENSHTEIN)
            return getStringDistance(expectArrStr, actualArrStr);

        if (arrDisType == ARRAY_DISTANCE_TYPE.FOR_EACH) {
            double distance = 0;
            Gson gson = new Gson();
            String[] expect = gson.fromJson(expectArrStr, String[].class);
            String[] actual = gson.fromJson(actualArrStr, String[].class);
            int shorter = Math.min(expect.length, actual.length);
            int longer = Math.max(expect.length, actual.length);
            for (int i = 0; i < shorter; i++) {
                distance += normalize(getStringDistance(expect[i], actual[i]), ALPHA);
            }
            distance += (longer - shorter);
            return longer == 0 ? 0 : distance / longer;
        }

        return -1;
    }



}
