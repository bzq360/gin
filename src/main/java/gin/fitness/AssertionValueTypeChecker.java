package gin.fitness;

public class AssertionValueTypeChecker {

    public static AssertionValueType checkType(String str) {
        if (str == null) return AssertionValueType.NULL;
        if (isBoolean(str)) return AssertionValueType.BOOLEAN;
        if (isDouble(str)) return AssertionValueType.DOUBLE;
        return AssertionValueType.STRING;
    }

    public static boolean isBoolean(String str) {
        if (str == null) {
            return false;
        }
        return str.equalsIgnoreCase("true")
                || str.equalsIgnoreCase("false");
    }

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


    // TODO: distinguish types of array (Integer, String...)
    public static boolean isArray(String str) {
        return false;
    }

}
