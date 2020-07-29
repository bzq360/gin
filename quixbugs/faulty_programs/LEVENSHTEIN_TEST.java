package faulty_programs;


import org.junit.Test;

import static org.junit.Assert.*;

public class LEVENSHTEIN_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"electron",(String)"neutron");
        assertEquals( (int) 3, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"kitten",(String)"sitting");
        assertEquals( (int) 3, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"rosettacode",(String)"raisethysword");
        assertEquals( (int) 8, result);
    }

    @org.junit.Test(timeout = 40000)
    @org.junit.Ignore
    public void test_3() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"amanaplanacanalpanama",(String)"docnoteidissentafastneverpreventsafatnessidietoncod");
        assertEquals( (int) 42, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"abcdefg",(String)"gabcdef");
        assertEquals( (int) 2, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"",(String)"");
        assertEquals( (int) 0, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_6() throws Exception {
        int result = LEVENSHTEIN.levenshtein((String)"hello",(String)"olleh");
        assertEquals( (int) 4, result);
    }

    // evosuite

    @Test(timeout = 4000)
    public void test_7()  throws Throwable  {
        int int0 = LEVENSHTEIN.levenshtein("  o", "");
        assertEquals(3, int0);

        LEVENSHTEIN lEVENSHTEIN0 = new LEVENSHTEIN();
        LEVENSHTEIN.levenshtein("", "");
        int int1 = LEVENSHTEIN.levenshtein("", "");
        assertFalse(int1 == int0);
    }

    @Test(timeout = 4000)
    public void test_8()  throws Throwable  {
        LEVENSHTEIN.levenshtein("k+", "k+");
        String string0 = ":ie|;i\"";
        LEVENSHTEIN.levenshtein("k+", ":ie|;i\"");
        LEVENSHTEIN lEVENSHTEIN0 = new LEVENSHTEIN();
        String string1 = "Q<_4\"Lc)";
        LEVENSHTEIN.levenshtein("k+", "Q<_4\"Lc)");
        // Undeclared exception!
        LEVENSHTEIN.levenshtein(":ie|;i\"", "Q<_4\"Lc)");
    }

    @Test(timeout = 4000)
    public void test_9()  throws Throwable  {
        String string0 = "jh(}nc";
        LEVENSHTEIN.levenshtein("jh(}nc", "jh(}nc");
        // Undeclared exception!
        LEVENSHTEIN.levenshtein("jh(}nc", "java_programs.LEVENSHTEIN");
    }

    @Test(timeout = 4000)
    public void test_10()  throws Throwable  {
        LEVENSHTEIN lEVENSHTEIN0 = new LEVENSHTEIN();
        // Undeclared exception!
        try {
            LEVENSHTEIN.levenshtein((String) null, "ilxGfJZu");
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

