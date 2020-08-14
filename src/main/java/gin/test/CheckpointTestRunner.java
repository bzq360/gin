package gin.test;

import gin.Patch;
import gin.SourceFileTree;
import gin.checkpoints.CheckpointUtils;
import org.mdkt.compiler.CompiledCode;

import java.util.List;

public class CheckpointTestRunner extends InternalTestRunner {

    protected boolean outputCheckpoints = false;

    public CheckpointTestRunner(String fullyQualifiedClassName, String classPath, List<UnitTest> unitTests) {
        super(fullyQualifiedClassName, classPath, unitTests);
    }

    public CheckpointTestRunner(String fullyQualifiedClassName, String classPath, String testClassName) {
        super(fullyQualifiedClassName, classPath, testClassName);
    }

    public UnitTestResultSet runTests(Patch patch, int reps) {

        // Create a new class loader for every compilation, otherwise java will cache the modified class for us
        classLoader = new CacheClassLoader(this.getClassPath());

//        System.out.println("before apply:-------------------------------------------------");
//        System.out.println(patch.getSourceFile());

        // Apply the patch.
        String patchedSource = patch.apply();
        boolean patchValid = patch.lastApplyWasValid();
        List<Boolean> editsValid = patch.getEditsInvalidOnLastApply();

//        System.out.println("after apply:-------------------------------------------------");
//        System.out.println(patch.getAppliedSourceFile());

        // Did the code change as a result of applying the patch?
        boolean noOp = isPatchedSourceSame(patch.getSourceFile().toString(), patchedSource);

        // insert checkpoints
        CheckpointUtils.setup(outputCheckpoints);
        SourceFileTree sf = CheckpointUtils.insertCheckpoints((SourceFileTree) patch.getAppliedSourceFile());
        String appliedPatchSource = sf.getSource();

//        System.out.println("after checkpoints:-------------------------------------------------");
//        System.out.println(sf);

        // Compile
        CompiledCode code = null;
        //if (patchValid) { // // might be invalid due to a couple of edits, which drop to being no-ops; remaining edits might be ok so try compiling
        code = Compiler.compile(this.getClassName(), appliedPatchSource, this.getClassPath());
        //}
        boolean compiledOK = (code != null);

        // Add to class loader and run tests
        List<UnitTestResult> results = null;
        if (compiledOK) {
            classLoader.setCustomCompiledCode(this.getClassName(), code.getByteCode());
            results = runTests(reps, classLoader);
        }

        if (!patchValid || !compiledOK) {
            results = emptyResults(reps);
        }

        return new UnitTestResultSet(patch, patchValid, editsValid, compiledOK, noOp, results);

    }

}
