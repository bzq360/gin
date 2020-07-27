package gin.fitness;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssertionValueTypeCheckerTest {

    // boolean
    @Test
    public void string_arr_checkTypeTest0() {
        assertEquals(AssertionValueTypeChecker.checkType("[4,9,9,*,+,10,-,13,+]"), AssertionValueType.STRING_ARRAY);
    }

    // boolean
    @Test
    public void boolean_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("true", "false"), AssertionValueType.BOOLEAN);
    }

    @Test
    public void boolean_checkTypesTest1() {
        assertEquals(AssertionValueTypeChecker.checkTypes("TRUE", "TRUE"), AssertionValueType.BOOLEAN);
    }

    @Test
    public void boolean_checkTypesTest2() {
        assertEquals(AssertionValueTypeChecker.checkTypes("False", "true"), AssertionValueType.BOOLEAN);
    }

    // double
    @Test
    public void double_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("5", "6.1"), AssertionValueType.DOUBLE);
    }

    @Test
    public void double_checkTypesTest1() {
        assertEquals(AssertionValueTypeChecker.checkTypes("2.2", "3.0"), AssertionValueType.DOUBLE);
    }

    @Test
    public void double_checkTypesTest2() {
        assertEquals(AssertionValueTypeChecker.checkTypes("2.3", "9.23"), AssertionValueType.DOUBLE);
    }

    @Test
    public void double_checkTypesTest3() {
        assertEquals(AssertionValueTypeChecker.checkTypes("2", "3.0"), AssertionValueType.DOUBLE);
    }

    @Test
    public void double_checkTypesTest4() {
        assertEquals(AssertionValueTypeChecker.checkTypes("-2", "3.0"), AssertionValueType.DOUBLE);
    }

    // int
    @Test
    public void int_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("-2", "3"), AssertionValueType.INT);
    }


    // int array
    @Test
    public void int_arr_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("[1, 2]", "[5,-6.0]"), AssertionValueType.INT_ARRAY);
    }

    // double array
    @Test
    public void double_arr_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("[1,-2.2]", "[ 5 ]"), AssertionValueType.DOUBLE_ARRAY);
    }

    // string array
    @Test
    public void string_arr_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("[4,-9,9,*,+,10,-,13,+]", "[5]"), AssertionValueType.STRING_ARRAY);
    }

    // string
    @Test
    public void string_checkTypesTest0() {
        assertEquals(AssertionValueTypeChecker.checkTypes("ergrtwnwyn", "3"), AssertionValueType.STRING);
    }

    @Test
    public void string_checkTypesTest1() {
        assertEquals(AssertionValueTypeChecker.checkTypes("ergrtwnwyn", "[3]"), AssertionValueType.STRING);
    }
}