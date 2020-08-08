package gin.util;

import gin.Patch;
import gin.fitness.ARJAeFitnessCalculator;
import gin.test.UnitTest;
import gin.test.UnitTestResultSet;

import java.util.List;

public class GPArjaEFix extends GPFix {

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPArjaEFix(String[] args) {
        super(args);
    }

    /*============== Implementation of abstract methods  ==============*/

    @Override
    protected UnitTestResultSet initFitness(String className, List<UnitTest> tests, Patch origPatch) {
        return testPatch(className, tests, origPatch);
    }

    // Calculate fitness
    @Override
    protected double fitness(UnitTestResultSet results) {
        return ARJAeFitnessCalculator.calculate(results);
    }

    @Override
    protected boolean fitnessThreshold(UnitTestResultSet results, double orig) {
        return results.getCleanCompile();
    }

    @Override
    protected double compareFitness(double newFitness, double oldFitness) {
        return oldFitness - newFitness;
    }

}
