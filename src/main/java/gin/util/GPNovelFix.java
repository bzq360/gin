package gin.util;

import gin.fitness.NovelFitnessCalculator;
import gin.test.UnitTestResultSet;


public class GPNovelFix extends GPSimple {

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPNovelFix(String[] args) {
        super(args);
    }

    /*============== Implementation of abstract methods  ==============*/

    // Calculate fitness
    protected double fitness(UnitTestResultSet results) {
        return NovelFitnessCalculator.calculate(results);
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
