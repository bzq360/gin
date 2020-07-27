package gin.util;

import java.io.File;
import java.util.List;

import gin.SourceFile;
import gin.test.UnitTest;
import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;


/**
 * Method-based GPFix search.
 */

public class GPFix extends GPSimple {

    public static void main(String[] args) {
        GPFix sampler = new GPFix(args);
        sampler.sampleMethods();
    }

    public GPFix(String[] args) {
        super(args);
    }

    // Constructor used for testing
    public GPFix(File projectDir, File methodFile) {
        super(projectDir, methodFile);
    }

    // Use parent's search strategy
    @Override
    protected void search(String className, List<UnitTest> tests, SourceFile sourceFile) {
        super.search(className, tests, sourceFile);
    }

    /*============== Implementation of abstract methods  ==============*/

    // Calculate fitness
    protected double fitness(UnitTestResultSet results) {
        long testsFailed = 0;
        for (UnitTestResult res : results.getResults()) {
            if (!res.getPassed()) {
                testsFailed++;
            }
        }
        return testsFailed;
    }

    // Calculate fitness threshold, for selection to the next generation
    protected boolean fitnessThreshold(UnitTestResultSet results, double orig) {
        double newFit = fitness(results);
        return newFit <= orig;
    }

    // Compare two fitness values, result of comparison printed on commandline if > 0
    protected double compareFitness(double newFitness, double best) {
        return best - newFitness;
    }

} 
