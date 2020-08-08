package gin.util;

import gin.Patch;
import gin.fitness.NovelFitnessCalculator;
import gin.test.UnitTest;
import gin.test.UnitTestResultSet;

import java.util.List;


public class GPNovelFix extends GPFix {

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPNovelFix(String[] args) {
        super(args);
    }

    @Override
    protected UnitTestResultSet initFitness(String className, List<UnitTest> tests, Patch origPatch) {
        return null;
    }

    /*============== Implementation of abstract methods  ==============*/

    // Calculate fitness
    protected double fitness(UnitTestResultSet results) {
        return NovelFitnessCalculator.calculate(results);
    }

}
