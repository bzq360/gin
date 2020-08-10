package gin.util;

import com.sampullara.cli.Argument;
import gin.Patch;
import gin.checkpoints.CheckpointUtils;
import gin.test.UnitTest;
import gin.test.UnitTestResult;
import gin.test.UnitTestResultSet;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.*;

public class GPCheckpointFix extends GPSimple {

    @Argument(alias = "M", description = "Method file for reference program, required", required = true)
    protected String refMethodFilePath;

    @Argument(alias = "C", description = "output checkpoint values into csv files")
    protected boolean outputCheckpoints = false;

    // initial faulty program results
    private Map<UnitTest, Boolean> origTestResults = new HashMap<>();

    private int origPassing = 0;
    private int origFailing = 0;

    public static void main(String[] args) {
        GPCheckpointFix sampler = new GPCheckpointFix(args);
        sampler.sampleMethods();
    }

    /*============== Constructors  ==============*/

    public GPCheckpointFix(String[] args) {
        super(args);
    }

    public GPCheckpointFix(File projectDir, File methodFile) {
        super(projectDir, methodFile);
    }

    /*============== modified search for checkpoints  ==============*/

    protected void search(TargetMethod method, Patch origPatch) {

        // generate reference checkpoints
        generateReferenceCheckpoints();

        String className = method.getClassName();
        String methodName = method.toString();
        List<UnitTest> tests = method.getGinTests();

        // Run original faulty program
        UnitTestResultSet results = initFitness(className, tests, origPatch);

        // Calculate fitness and record result, including fitness improvement (currently 0)
        double orig = fitness(results);
        super.writePatch(results, methodName, orig, 0);

        // Keep best
        double best = orig;

        // Generation 1
        Map<Patch, Double> population = new HashMap<>();
        population.put(origPatch, orig);

        for (int i = 1; i < indNumber; i++) {

            // Add a mutation
            Patch patch = mutate(origPatch);
            // If fitnessThreshold met, add it
            results = testPatchWithCheckpoints(className, tests, patch);
            double fitness = fitness(results);
            if (fitnessThreshold(results, orig)) {
                population.put(patch, fitness);
            }

        }

        for (int g = 0; g < genNumber; g++) {

            // Previous generation
            List<Patch> patches = new ArrayList(population.keySet());

            Logger.info("Creating generation: " + (g + 1));

            // Current generation
            Map<Patch, Double> newPopulation = new HashMap<>();

            // Select individuals for crossover
            List<Patch> selectedPatches = select(population, origPatch, orig);

            // Keep a list of patches after crossover
            List<Patch> crossoverPatches = crossover(selectedPatches, origPatch);

            // If less than indNumber variants produced, add random patches from the previous generation
            while (crossoverPatches.size() < indNumber) {
                crossoverPatches.add(patches.get(super.individualRng.nextInt(patches.size())).clone());
            }

            // Mutate the newly created population and check fitness
            for (Patch patch : crossoverPatches) {

                // Add a mutation
                patch = mutate(patch);

                Logger.debug("Testing patch: " + patch);

                // Test the patched source file
                results = testPatchWithCheckpoints(className, tests, patch);
                double newFitness = fitness(results);

                // If fitness threshold met, add patch to the mating population
                if (fitnessThreshold(results, orig)) {
                    newPopulation.put(patch, newFitness);
                }
                super.writePatch(results, methodName, newFitness, compareFitness(newFitness, orig));
            }

            population = new HashMap<Patch, Double>(newPopulation);
            if (population.isEmpty()) {
                population.put(origPatch, orig);
            }

        }
    }

    protected void generateReferenceCheckpoints() {
        // init checkpoint utility
        CheckpointUtils.setup(outputCheckpoints);
        // run programs and generate checkpoint values
        String[] args = {"-d", projectDirectory.getName(), "-c", classPath, "-m", refMethodFilePath, "-o", "reference_results.csv"};
        new ReferenceCheckpointsGenerator(args).sampleMethodsHook();
        // set the generated checkpoints as reference checkpoints
        CheckpointUtils.setupOriginalCheckpoints();
    }

    protected UnitTestResultSet testPatchWithCheckpoints(String targetClass, List<UnitTest> tests, Patch patch) {
        CheckpointUtils.setup(outputCheckpoints);
        Patch patchCP = CheckpointUtils.insertCheckpoints(patch);
        return testPatch(targetClass, tests, patchCP);
    }


    /*============== Implementation of abstract methods  ==============*/

    @Override
    protected UnitTestResultSet initFitness(String className, List<UnitTest> tests, Patch origPatch) {
        super.reps = 1;

        CheckpointUtils.setup(outputCheckpoints);
        Patch origPatchCP = CheckpointUtils.insertCheckpoints(origPatch);

        UnitTestResultSet results = testPatch(className, tests, origPatchCP);
        for (UnitTestResult result : results.getResults()) {
            if (result.getPassed()) {
                origTestResults.put(result.getTest(), true);
                origPassing++;
            } else {
                origTestResults.put(result.getTest(), false);
                origFailing++;
            }
        }
        return results;
    }

    @Override
    protected double fitness(UnitTestResultSet results) {
        double posScore = 0, negScore = 0;

        // counting passing and failing test cases
        double passing = 0, failing = 0;
        for (UnitTestResult result : results.getResults()) {
            if (result.getPassed()) {
                if (origTestResults.get(result.getTest())) {
                    passing++;
                } else {
                    failing++;
                }
            }
        }
        // compute alpha and beta
        double alpha = passing / origPassing;
        double beta = failing / origFailing;

        if (alpha == 1 && beta == 1) {
            return 1;
        }
        double weight = (alpha == 0) ? 0.05 : 0.5;

        // compute checkpoint distance for each test case
        int index = 0;
        for (UnitTestResult result : results.getResults()) {
            boolean origPass = origTestResults.get(result.getTest());
            boolean currPass = result.getPassed();
            double distance = CheckpointUtils.computeDistance(index, origPass, currPass);
            if (origPass) {
                posScore += distance;
            } else {
                negScore += distance;
            }
        }

        return weight * (posScore + negScore);
    }

    @Override
    protected boolean fitnessThreshold(UnitTestResultSet results, double orig) {
        return results.getCleanCompile();
    }

    @Override
    protected double compareFitness(double newFitness, double oldFitness) {
        return newFitness - oldFitness;
    }
}
