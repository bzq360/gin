package faulty_programs;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SIEVE_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)1);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)2);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[2]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)4);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[2,3]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_3() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)7);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[2,3,5,7]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)20);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[2,3,5,7,11,13,17,19]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        java.util.ArrayList result = SIEVE.sieve((Integer)50);
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[2,3,5,7,11,13,17,19,23,29,31,37,41,43,47]", resultFormatted);
    }

    // EVOSUITE

    @Test(timeout = 4000)
    public void test_6()  throws Throwable  {
        SIEVE sIEVE0 = new SIEVE();
    }

    @Test(timeout = 4000)
    public void test_7()  throws Throwable  {
        Integer integer0 = new Integer(3564);
        // Undeclared exception!
        SIEVE.sieve(integer0);
    }

    @Test(timeout = 4000)
    public void test_8()  throws Throwable  {
        ArrayList<Boolean> arrayList0 = new ArrayList<Boolean>();
        Boolean boolean0 = new Boolean("%w[/B/7xE4rLd}Kk8{=");
        arrayList0.add(boolean0);
        boolean boolean1 = SIEVE.any(arrayList0);
        assertFalse(boolean1);
    }

    @Test(timeout = 4000)
    public void test_9()  throws Throwable  {
        ArrayList<Boolean> arrayList0 = new ArrayList<Boolean>();
        Boolean boolean0 = Boolean.valueOf(true);
        arrayList0.add(boolean0);
        boolean boolean1 = SIEVE.any(arrayList0);
        assertTrue(boolean1);
    }

    @Test(timeout = 4000)
    public void test_10()  throws Throwable  {
        // Undeclared exception!
        try {
            SIEVE.all((ArrayList<Boolean>) null);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test(timeout = 4000)
    public void test_11()  throws Throwable  {
        // Undeclared exception!
        try {
            SIEVE.any((ArrayList<Boolean>) null);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test(timeout = 4000)
    public void test_12()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        Integer integer0 = new Integer(0);
        arrayList0.add(integer0);
        // Undeclared exception!
        try {
            SIEVE.list_comp(0, arrayList0);
            fail("Expecting exception: ArithmeticException");

        } catch(ArithmeticException e) {
            //
            // / by zero
            //
        }
    }

    @Test(timeout = 4000)
    public void test_13()  throws Throwable  {
        // Undeclared exception!
        try {
            SIEVE.list_comp((-1), (ArrayList<Integer>) null);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test(timeout = 4000)
    public void test_14()  throws Throwable  {
        // Undeclared exception!
        try {
            SIEVE.sieve((Integer) null);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test(timeout = 4000)
    public void test_15()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        ArrayList<Boolean> arrayList1 = SIEVE.list_comp(2444, arrayList0);
        Boolean boolean0 = Boolean.valueOf("");
        arrayList1.add(boolean0);
        boolean boolean1 = SIEVE.all(arrayList1);
        assertFalse(boolean1);
        assertTrue(arrayList0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test_16()  throws Throwable  {
        ArrayList<Boolean> arrayList0 = new ArrayList<Boolean>();
        boolean boolean0 = SIEVE.all(arrayList0);
        assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test_17()  throws Throwable  {
        Integer integer0 = new Integer(46);
        ArrayList<Integer> arrayList0 = SIEVE.sieve(integer0);
        assertEquals(14, arrayList0.size());
    }

    @Test(timeout = 4000)
    public void test_18()  throws Throwable  {
        Integer integer0 = new Integer((-917));
        ArrayList<Integer> arrayList0 = SIEVE.sieve(integer0);
        boolean boolean0 = arrayList0.add(integer0);
        ArrayList<Boolean> arrayList1 = SIEVE.list_comp((-2137), arrayList0);
        assertFalse(arrayList1.contains(boolean0));
        assertEquals(1, arrayList1.size());
    }
}

