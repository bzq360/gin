package faulty_programs;

import org.junit.Test;

import static org.junit.Assert.*;

public class IS_VALID_PARENTHESIZATION_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        Boolean result = IS_VALID_PARENTHESIZATION.is_valid_parenthesization((String)"((()()))()");
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("true", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        Boolean result = IS_VALID_PARENTHESIZATION.is_valid_parenthesization((String)")()(");
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("false", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        Boolean result = IS_VALID_PARENTHESIZATION.is_valid_parenthesization((String)"((");
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("false", resultFormatted);
    }

    // Evosuite

    @Test(timeout = 4000)
    public void test_3()  throws Throwable  {
        Boolean boolean0 = IS_VALID_PARENTHESIZATION.is_valid_parenthesization("((ah");
        assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test_4()  throws Throwable  {
        Boolean boolean0 = IS_VALID_PARENTHESIZATION.is_valid_parenthesization("(1|>h~|\"qV?j:hE-g");
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test_5()  throws Throwable  {
        Boolean boolean0 = IS_VALID_PARENTHESIZATION.is_valid_parenthesization("(");
        assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test_6()  throws Throwable  {
        // Undeclared exception!
        try {
            IS_VALID_PARENTHESIZATION.is_valid_parenthesization((String) null);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

