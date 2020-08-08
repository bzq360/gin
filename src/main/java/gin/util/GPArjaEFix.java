package gin.util;

import gin.fitness.ARJAeFitnessCalculator;
import gin.test.UnitTestResultSet;

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

    // Calculate fitness
    protected double fitness(UnitTestResultSet results) {
        return ARJAeFitnessCalculator.calculate(results);
    }

}
