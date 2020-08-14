package gin.util;

import gin.Patch;
import gin.SourceFileTree;
import gin.checkpoints.CheckpointUtils;
import gin.test.UnitTestResultSet;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReferenceCheckpointsGenerator extends Sampler {

    public ReferenceCheckpointsGenerator(String[] args) {
        super(args);
        setUp();
    }

    protected void sampleMethodsHook() {
        writeHeader();

        // Get the first method
        TargetMethod method = super.methodData.get(0);

        // Get method location
        File source = method.getFileSource();
        String className = method.getClassName();

        // Create source file for line edits for the example method
        SourceFileTree sourceFile = new SourceFileTree(source, method.getMethodName());
        Patch refPatchCP = new Patch(CheckpointUtils.insertCheckpoints(sourceFile));

        Logger.info("Running tests on the reference program");

        // Run all project tests (example sourceFile and className needed for TestRunner setup)
        UnitTestResultSet results = testPatch(className, new ArrayList<>(super.testData), refPatchCP);

        writeResults(results);
        try {
            outputFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.info("Results saved to: " + super.outputFile.getAbsolutePath());
    }

}
