package gin.fitness;

import com.google.gson.Gson;

public class AssertionValueTypeChecker {

    public static AssertionValueType checkType(String s) {
        if (s == null) return AssertionValueType.NULL;
        if (isBoolean(s)) return AssertionValueType.BOOLEAN;
        if (isInt(s)) return AssertionValueType.INT;
        if (isDouble(s)) return AssertionValueType.DOUBLE;
        if (isIntArray(s)) return AssertionValueType.INT_ARRAY;
        if (isDoubleArray(s)) return AssertionValueType.DOUBLE_ARRAY;
        if (isStringArray(s)) return AssertionValueType.STRING_ARRAY;
        return AssertionValueType.STRING;
    }

    public static AssertionValueType checkTypes(String a, String b) {
        // if any one type is null, return null
        if (checkType(a) == AssertionValueType.NULL || checkType(b) == AssertionValueType.NULL)
            return AssertionValueType.NULL;

        // if both types are boolean, return boolean
        if (checkType(a) == AssertionValueType.BOOLEAN && checkType(b) == AssertionValueType.BOOLEAN) {
            return AssertionValueType.BOOLEAN;
        }

        // if both types are int, return int
        if (checkType(a) == AssertionValueType.INT && checkType(b) == AssertionValueType.INT) {
            return AssertionValueType.INT;
        }

        // if both types are double, return double
        if ((checkType(a) == AssertionValueType.DOUBLE && checkType(b) == AssertionValueType.DOUBLE)
                || (checkType(a) == AssertionValueType.INT && checkType(b) == AssertionValueType.DOUBLE)
                || (checkType(a) == AssertionValueType.DOUBLE && checkType(b) == AssertionValueType.INT))
            return AssertionValueType.DOUBLE;

        // if both types are int array, return int array
        if (checkType(a) == AssertionValueType.INT_ARRAY && checkType(b) == AssertionValueType.INT_ARRAY)
            return AssertionValueType.INT_ARRAY;

        // if one type is int array and the other is double array, or both types are double array, return double array
        if ((checkType(a) == AssertionValueType.DOUBLE_ARRAY && checkType(b) == AssertionValueType.INT_ARRAY)
                || (checkType(a) == AssertionValueType.INT_ARRAY && checkType(b) == AssertionValueType.DOUBLE_ARRAY)
                || (checkType(a) == AssertionValueType.DOUBLE_ARRAY && checkType(b) == AssertionValueType.DOUBLE_ARRAY))
            return AssertionValueType.DOUBLE_ARRAY;

        // if both types are string array, return string array
        if (checkType(a) == AssertionValueType.STRING_ARRAY && isArray(b)
                || (checkType(b) == AssertionValueType.STRING_ARRAY && isArray(a)))
            return AssertionValueType.STRING_ARRAY;

        // if any type is string, return string
        if (checkType(a) == AssertionValueType.STRING || checkType(b) == AssertionValueType.STRING)
            return AssertionValueType.STRING;

        return AssertionValueType.NOT_MATCHED;
    }


    public static boolean isBoolean(String str) {
        if (str == null) {
            return false;
        }
        return str.equalsIgnoreCase("true")
                || str.equalsIgnoreCase("false");
    }

    // "2.0" is not int
    public static boolean isInt(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isIntArray(String str) {
        Gson gson = new Gson();
        try {
            int[] arr = gson.fromJson(str, int[].class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDoubleArray(String str) {
        Gson gson = new Gson();
        try {
            double[] arr = gson.fromJson(str, double[].class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isStringArray(String str) {
        Gson gson = new Gson();
        try {
            String[] arr = gson.fromJson(str, String[].class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isArray(String str) {
        return isIntArray(str) || isDoubleArray(str) || isStringArray(str);
    }

}
