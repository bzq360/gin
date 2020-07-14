package gin.util;

import com.opencsv.CSVWriter;
import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import gin.Patch;
import gin.SourceFile;
import gin.edit.Edit;
import gin.edit.Edit.EditType;
import gin.fitness.Fitness;
import gin.fitness.FitnessType;
import gin.test.UnitTest;
import gin.test.UnitTestResultSet;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class GPNovel extends Sampler {

    @Argument(alias = "et", description = "Edit type: this can be a member of the EditType enum (LINE,STATEMENT,MATCHED_STATEMENT,MODIFY_STATEMENT); the fully qualified name of a class that extends gin.edit.Edit, or a comma separated list of both")
    protected String editType = EditType.MATCHED_STATEMENT.toString();

    @Argument(alias = "gn", description = "Number of generations")
    protected Integer genNumber = 1;

    @Argument(alias = "in", description = "Number of individuals")
    protected Integer indNumber = 10;

    @Argument(alias = "ms", description = "Random seed for mutation selection")
    protected Integer mutationSeed = 123;

    @Argument(alias = "is", description = "Random seed for individual selection")
    protected Integer individualSeed = 123;

    // fitness type
    @Argument(alias = "ft", description = "this can be a member of the FitnessType enum (RefinedFitness)")
    protected FitnessType fitnessType = FitnessType.DECISION;

//    // normalization parameter
//    @Argument(alias = "al", description = "normalization function parameter: v(x) = x / (x + alpha)")
//    protected Double alpha = -1.0;

    /**allowed edit types for sampling: parsed from editType*/
    protected List<Class<? extends Edit>> editTypes;


    protected Random mutationRng;
    protected Random individualRng;

    public GPNovel(String[] args) {
        super(args);
        Args.parseOrExit(this, args);
        editTypes = Edit.parseEditClassesFromString(editType);
        printAdditionalArguments();
        setup();
    }

    // Constructor used for testing
    public GPNovel(File projectDir, File methodFile) {
        super(projectDir, methodFile);
        editTypes = Edit.parseEditClassesFromString(editType);
    }

    private void printAdditionalArguments() {
        Logger.info("Edit types: "+ editTypes);
        Logger.info("Number of generations: "+ genNumber);
        Logger.info("Number of individuals: "+ indNumber);
        Logger.info("Random seed for mutation selection: "+ mutationSeed);
        Logger.info("Random seed for individual selection: "+ individualSeed);
    }

    private void setup() {
        mutationRng = new Random(mutationSeed);
        individualRng = new Random(individualSeed);
    }

    // Implementation of the abstract method
    @Override
    protected void sampleMethodsHook() {
        writeNewHeader();

        for (TargetMethod method : methodData) {

            Logger.info("Running GP on method " + method);

            // Setup SourceFile for patching
            SourceFile sourceFile = SourceFile.makeSourceFileForEditTypes(editTypes, method.getFileSource().getPath(), Collections.singletonList(method.getMethodName()));

            search(method.getClassName(), method.getGinTests(), sourceFile, fitnessType);
        }
    }

    /*============== Abstract methods  ==============*/

    // Simple patch selection
    protected abstract Patch select(List<Patch> patches);

    // Mutation operator
    protected abstract Patch mutate(Patch oldPatch);

    // Crossover operator
    protected abstract List<Patch> createCrossoverPatches(List<Patch> patches, SourceFile sourceFile);

    // GP search strategy
    protected abstract void search(String className, List<UnitTest> tests, SourceFile sourceFile, FitnessType fitnessType);

    // Calculate fitness threshold, for selection to the next generation
    protected abstract boolean fitnessThreshold(Fitness newFitness, Fitness oldFitness);

    // Compare two fitness values
    protected abstract double compareFitness(Fitness newFitness, Fitness oldFitness);

    /*============== Helper methods  ==============*/

    protected void writeNewHeader() {
        String[] entry = {"ClassName"
                , "Patch"
                , "Compiled"
                , "AllTestsPassed"
                , "TotalExecutionTime(ms)"
                , "Fitness"
                , "FitnessImprovement"
        };
        try {
            outputFileWriter = new CSVWriter(new FileWriter(outputFile));
            outputFileWriter.writeNext(entry);
        } catch (IOException e) {
            Logger.error(e, "Exception writing results to the output file: " + outputFile.getAbsolutePath());
            Logger.trace(e);
            System.exit(-1);
        }
    }

    protected void writePatch(UnitTestResultSet results, String className, Fitness fitness) {
        String[] entry = {className
                , results.getPatch().toString()
                , Boolean.toString(results.getCleanCompile())
                , Boolean.toString(results.allTestsSuccessful())
                , Float.toString(results.totalExecutionTime() / 1000000.0f)
                , fitness.toString()
                , "improvement placeholder" // TODO: implement improvement
        };
        outputFileWriter.writeNext(entry);
    }
}
