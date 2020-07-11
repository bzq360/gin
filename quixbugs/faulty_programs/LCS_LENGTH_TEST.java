package faulty_programs;

import faulty_programs.LCS_LENGTH;

public class LCS_LENGTH_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"witch",(String)"sandwich");
        org.junit.Assert.assertEquals( (Integer) 2, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"meow",(String)"homeowner");
        org.junit.Assert.assertEquals( (Integer) 4, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"fun",(String)"");
        org.junit.Assert.assertEquals( (Integer) 0, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_3() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"fun",(String)"function");
        org.junit.Assert.assertEquals( (Integer) 3, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"cyborg",(String)"cyber");
        org.junit.Assert.assertEquals( (Integer) 3, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"physics",(String)"physics");
        org.junit.Assert.assertEquals( (Integer) 7, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_6() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"space age",(String)"pace a");
        org.junit.Assert.assertEquals( (Integer) 6, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_7() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"flippy",(String)"floppy");
        org.junit.Assert.assertEquals( (Integer) 3, result);
    }

    @org.junit.Test(timeout = 3000)
    public void test_8() throws Exception {
        Integer result = LCS_LENGTH.lcs_length((String)"acbdegcedbg",(String)"begcfeubk");
        org.junit.Assert.assertEquals( (Integer) 3, result);
    }
}

