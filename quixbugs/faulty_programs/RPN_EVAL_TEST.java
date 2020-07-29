package faulty_programs;


import org.junit.Test;

import java.util.ArrayList;
import java.util.EmptyStackException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RPN_EVAL_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(3.0,5.0,"+",2.0,"/")));
        assertEquals( (Double) 4.0, result, 0.0);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(2.0,2.0,"+")));
        assertEquals( (Double) 4.0, result, 0.0);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(7.0,4.0,"+",3.0,"-")));
        assertEquals( (Double) 8.0, result, 0.0);
    }

    @org.junit.Test(timeout = 3000)
    public void test_3() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(1.0,2.0,"*",3.0,4.0,"*","+")));
        assertEquals( (Double) 14.0, result, 0.0);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(5.0,9.0,2.0,"*","+")));
        assertEquals( (Double) 23.0, result, 0.0);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        Double result = RPN_EVAL.rpn_eval(new java.util.ArrayList(java.util.Arrays.asList(5.0,1.0,2.0,"+",4.0,"*","+",3.0,"-")));
        assertEquals( (Double) 14.0, result, 0.0);
    }

    // evosuite

    @Test(timeout = 4000)
    public void test_6()  throws Throwable  {
        RPN_EVAL rPN_EVAL0 = new RPN_EVAL();
    }

    @Test(timeout = 4000)
    public void test_7()  throws Throwable  {
        ArrayList<Object> arrayList0 = new ArrayList<Object>();
        arrayList0.add((Object) arrayList0);
        // Undeclared exception!
        try {
            RPN_EVAL.rpn_eval(arrayList0);
            fail("Expecting exception: ClassCastException");

        } catch(ClassCastException e) {
            //
            // java.util.ArrayList cannot be cast to java.lang.String
            //
        }
    }

    @Test(timeout = 4000)
    public void test_8()  throws Throwable  {
        ArrayList<Object> arrayList0 = new ArrayList<Object>();
        // Undeclared exception!
        try {
            RPN_EVAL.rpn_eval(arrayList0);
            fail("Expecting exception: EmptyStackException");

        } catch(EmptyStackException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test(timeout = 4000)
    public void test_9()  throws Throwable  {
        ArrayList<Object> arrayList0 = new ArrayList<Object>();
        Double double0 = new Double(1.0);
        arrayList0.add((Object) double0);
        Double double1 = RPN_EVAL.rpn_eval(arrayList0);
        assertEquals(1.0, (double)double1, 0.01);
    }

    @Test(timeout = 4000)
    public void test_10()  throws Throwable  {
        ArrayList<Double> arrayList0 = new ArrayList<Double>();
        Double double0 = new Double((-1589.74570351847));
        arrayList0.add(double0);
        Double double1 = RPN_EVAL.rpn_eval(arrayList0);
        assertEquals((-1589.74570351847), (double)double1, 0.01);
    }

    @Test(timeout = 4000)
    public void test_11()  throws Throwable  {
        ArrayList<Double> arrayList0 = new ArrayList<Double>();
        Double double0 = Double.valueOf(0.0);
        arrayList0.add(0, double0);
        Double double1 = RPN_EVAL.rpn_eval(arrayList0);
        assertEquals(0.0, (double)double1, 0.01);
    }

    @Test(timeout = 4000)
    public void test_12()  throws Throwable  {
        ArrayList<Object> arrayList0 = new ArrayList<Object>();
        Double double0 = new Double((-1241.3948790124489));
        arrayList0.add((Object) double0);
        Double double1 = new Double((-1241.3948790124489));
        arrayList0.add((Object) double1);
        arrayList0.add((Object) null);
        // Undeclared exception!
        try {
            RPN_EVAL.rpn_eval(arrayList0);
            fail("Expecting exception: NullPointerException");

        } catch(NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}

