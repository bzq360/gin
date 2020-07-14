package gin.fitness;

import gin.test.UnitTestResultSet;

public abstract class Fitness{

    public static Fitness generateFitness(FitnessType fitnessType, UnitTestResultSet results) {
        if (fitnessType == FitnessType.DECISION) {
            return new RefinedFitness(results);
        } else {
            throw new IllegalArgumentException("Not a supported fitness type");
        }
    }

    public static long compare(FitnessType fitnessType, Fitness m, Fitness n) {
        if (fitnessType == FitnessType.DECISION) {
            return ((RefinedFitness) m).compareTo((RefinedFitness) n);
        } else {
            throw new IllegalArgumentException("Not a supported fitness type");
        }
    }

    public abstract String toString();

}
