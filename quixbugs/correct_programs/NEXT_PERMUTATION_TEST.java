package correct_programs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class NEXT_PERMUTATION_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(3,2,4,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[3,4,1,2]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(3,5,6,2,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[3,6,1,2,5]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(3,5,6,2)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[3,6,2,5]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_3() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(4,5,1,7,9)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[4,5,1,9,7]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(4,5,8,7,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[4,7,1,5,8]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(9,5,2,6,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[9,5,6,1,2]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_6() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(44,5,1,7,9)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[44,5,1,9,7]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_7() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(3,4,5)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        assertEquals("[3,5,4]", resultFormatted);
    }

    // new passing tests
    @org.junit.Test(timeout = 3000)
    public void test_16() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(1,1,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_17() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(java.util.Arrays.asList(3,2,1)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[]", resultFormatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_18() throws Exception {
        java.util.ArrayList result = NEXT_PERMUTATION.next_permutation(new java.util.ArrayList(Collections.singletonList(0)));
        String resultFormatted = QuixFixOracleHelper.format(result,true);
        org.junit.Assert.assertEquals("[]", resultFormatted);
    }

    // evosuite

    public void test_8()  throws Throwable  {
        NEXT_PERMUTATION nEXT_PERMUTATION0 = new NEXT_PERMUTATION();
    }

    @Test(timeout = 4000)
    public void test_9()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        // Undeclared exception!
        try {
            NEXT_PERMUTATION.next_permutation(arrayList0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");

        } catch(ArrayIndexOutOfBoundsException e) {
            //
            // -2
            //
        }
    }

    @Test(timeout = 4000)
    public void test_10()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        Integer integer0 = new Integer((-3094));
        arrayList0.add(integer0);
        ArrayList<Integer> arrayList1 = NEXT_PERMUTATION.next_permutation(arrayList0);
        assertEquals(0, arrayList1.size());
    }

    @Test(timeout = 4000)
    public void test_11()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        Integer integer0 = new Integer((-1187));
        arrayList0.add(integer0);
        arrayList0.add(integer0);
        ArrayList<Integer> arrayList1 = NEXT_PERMUTATION.next_permutation(arrayList0);
        assertTrue(arrayList1.isEmpty());
    }

    @Test(timeout = 4000)
    public void test_12()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        Integer integer0 = new Integer((-1187));
        arrayList0.add(integer0);
        Integer integer1 = new Integer((-1));
        arrayList0.add(integer1);
        NEXT_PERMUTATION.next_permutation(arrayList0);
        assertEquals(2, arrayList0.size());
        assertTrue(arrayList0.contains((-1)));
    }

    @Test(timeout = 4000)
    public void test_13()  throws Throwable  {
        ArrayList<Integer> arrayList0 = new ArrayList<Integer>();
        Integer integer0 = new Integer((-1187));
        arrayList0.add(integer0);
        Integer integer1 = new Integer((-1));
        arrayList0.add(integer1);
        arrayList0.add(integer0);
        ArrayList<Integer> arrayList1 = NEXT_PERMUTATION.next_permutation(arrayList0);
        assertEquals(3, arrayList0.size());
        assertSame(arrayList0, arrayList1);
    }

    @Test(timeout = 4000)
    public void test_14()  throws Throwable  {
        // Undeclared exception!
        try {
            NEXT_PERMUTATION.next_permutation((ArrayList<Integer>) null);
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
        Integer integer0 = new Integer((-1187));
        arrayList0.add(integer0);
        Integer integer1 = new Integer((-1));
        arrayList0.add(integer1);
        Integer integer2 = new Integer(1);
        arrayList0.add(integer2);
        NEXT_PERMUTATION.next_permutation(arrayList0);
        ArrayList<Integer> arrayList1 = NEXT_PERMUTATION.next_permutation(arrayList0);
        ArrayList<Integer> arrayList2 = NEXT_PERMUTATION.next_permutation(arrayList1);
        ArrayList<Integer> arrayList3 = NEXT_PERMUTATION.next_permutation(arrayList2);
        assertEquals(3, arrayList0.size());
        assertSame(arrayList0, arrayList3);
    }
}

