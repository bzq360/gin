package gin.util;

import gin.Patch;
import gin.SourceFile;
import gin.edit.Edit;
import gin.fitness.Fitness;
import gin.fitness.FitnessType;

import gin.test.UnitTest;
import gin.test.UnitTestResultSet;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPNovelFix extends GPNovel {

    public static void main(String[] args) {
        GPNovelFix sampler = new GPNovelFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPNovelFix(String[] args) {
        super(args);
    }

    public GPNovelFix(File projectDir, File methodFile) {
        super(projectDir, methodFile);
    }

    /*============== Implementation of abstract methods  ==============*/

    // Simple GP search (based on GenProg)
    @Override
    protected void search(String className, List<UnitTest> tests, SourceFile sourceFile, FitnessType fitnessType) {

        Patch origPatch = new Patch(sourceFile);

        // Run original code
        UnitTestResultSet results = testPatch(className, tests, origPatch);

        // Calculate fitness and record result
        Fitness origFitness = Fitness.generateFitness(fitnessType, results);
        super.writePatch(results, className, origFitness);

        // Keep best
        Fitness bestFitness = origFitness;

        // Generation 0
        Map<Patch, Fitness> population = new HashMap<>();
        population.put(origPatch, origFitness);

        for (int g = 0; g < genNumber; g++) {

            // Previous generation
            List<Patch> patches = new ArrayList<>(population.keySet());

            Logger.info("Creating generation: " + (g + 1));

            // Current generation
            Map<Patch, Fitness> newPopulation = new HashMap<>();

            // Keep a list of patches after crossover
            List<Patch> crossoverPatches = createCrossoverPatches(patches, sourceFile);

            // If less than indNumber variants produced, add random patches from the previous generation
            while (crossoverPatches.size() < indNumber) {
                crossoverPatches.add(select(patches));
            }

            // Mutate the newly created population and check runtime
            for (Patch patch : crossoverPatches) {

                // Add a mutation
                patch = mutate(patch);

                // Test the patched source file
                results = testPatch(className, tests, patch);

                if (results.allTestsSuccessful()) {
                    Logger.info("Fix found: " + patch);
                    System.exit(0);
                }

                Fitness newFitness = Fitness.generateFitness(fitnessType, results);

                Logger.info("Fitness = " + newFitness + " ; Testing patch: " + patch);

                // If all tests pass, add patch to the mating population, check for new bestTime
                if (fitnessThreshold(newFitness, origFitness)) {
                    super.writePatch(results, className, newFitness);
                    newPopulation.put(patch, newFitness);
                    double better = compareFitness(newFitness, bestFitness);
                    if (better > 0) {
                        Logger.info("Better patch found: " + patch);
                        Logger.info("Fitness improvement over best found so far: " + better);
                        bestFitness = newFitness;
                    }
                } else {
                    super.writePatch(results, className, newFitness);
                }
            }

            population = new HashMap<>(newPopulation);
            if (population.isEmpty()) {
                population.put(origPatch, origFitness);
            }

        }

    }

    protected double compareFitness(Fitness m, Fitness n) {
        return Fitness.compare(fitnessType, m, n);
    }

    protected boolean fitnessThreshold(Fitness newFitness, Fitness oldFitness) {
        return Fitness.compare(fitnessType, newFitness, oldFitness) > 0;
    }

    // Simple patch selection, returns a clone of the selected patch
    protected Patch select(List<Patch> patches) {
        return patches.get(super.individualRng.nextInt(patches.size())).clone();
    }

    // Mutation operator, returns a clone of the old patch
    protected Patch mutate(Patch oldPatch) {
        Patch patch = oldPatch.clone();
        patch.addRandomEditOfClasses(super.mutationRng, super.editTypes);
        return patch;
    }

    // Returns a list of patches after crossover
    protected List<Patch> createCrossoverPatches(List<Patch> patches, SourceFile sourceFile) {

        List<Patch> crossoverPatches = new ArrayList<>();

        // Crossover produces four individuals
        for (int i = 0; i < super.indNumber / 4; i++) {

            // Select a patch from previous generation
            Patch patch1 = select(patches);
            crossoverPatches.add(patch1);
            Patch patch2 = select(patches);
            crossoverPatches.add(patch2);

            Patch patch3 = crossover(patch1, patch2, sourceFile);
            crossoverPatches.add(patch3);
            Patch patch4 = crossover(patch2, patch1, sourceFile);
            crossoverPatches.add(patch4);

        }

        return crossoverPatches;

    }

    /*============== Helper methods  ==============*/

    // Returns a patch which contains the first half of edits in patch1 and second half of edits in patch2
    private Patch crossover(Patch patch1, Patch patch2, SourceFile sourceFile) {
        List<Edit> list1 = patch1.getEdits();
        List<Edit> list2 = patch2.getEdits();
        Patch patch = new Patch(sourceFile);
        for (int i = 0; i < patch1.size() / 2; i++) {
            patch.add(list1.get(i));
        }
        for (int i = patch2.size() / 2; i < patch2.size(); i++) {
            patch.add(list2.get(i));
        }
        return patch;
    }


}
