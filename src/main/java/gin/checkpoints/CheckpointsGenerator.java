package gin.checkpoints;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.metamodel.ForeachStmtMetaModel;
import com.github.javaparser.metamodel.IntegerLiteralExprMetaModel;
import gin.SourceFileTree;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.javaparser.JavaParser.parseStatement;

public class CheckpointsGenerator {

    // method for test
    String src = "C:\\UCL\\IdeaProjects\\parser_test\\src\\main\\java\\MAX_SUBLIST_SUM.java";
    String methodName = "MAX_SUBLIST_SUM.max_sublist_sum(int[])";

    private final Map<Integer, Pair<String, Integer>> intVariables = new HashMap<>(); // <node id, (variable name, value)>

    public void run() {
        SourceFileTree sf = new SourceFileTree(new File(src), methodName);

        // store integer variable declarations
        List<Integer> intLst = sf.getNodeIDsByClass(true, VariableDeclarationExpr.class);
        for (int nid : intLst) {
            VariableDeclarationExpr ve = (VariableDeclarationExpr) sf.getNode(nid);
            // jump over parameter of the for loop
            if (ve.getParentNode().isPresent() && ve.getParentNode().get().getMetaModel() instanceof ForeachStmtMetaModel)
                continue;
            VariableDeclarator vd = (VariableDeclarator) ve.getChildNodes().get(0);
            String vName = vd.getNameAsString();
            for (Node node : vd.getChildNodes()) {
                if (node.getMetaModel() instanceof IntegerLiteralExprMetaModel) {
                    int value = ((IntegerLiteralExpr) node).asInt();
                    intVariables.put(nid, new Pair<>(vName, value));
                }
            }
        }

        // assume we have only 1 foreach stmt in this case
        List<Integer> forLst = sf.getNodeIDsByClass(true, ForeachStmt.class);
        int sid = forLst.get(0);
        int blockId = sf.getBlockIDsInTargetMethod().get(0);

        int cid = 0; // checkpoint id
        // insert checkpoints after for loop
        for (Map.Entry<Integer, Pair<String, Integer>> entry : intVariables.entrySet()) {
            sf = sf.insertStatement(blockId, sid, constructCheckpointStatement(cid++, entry.getValue().getKey()));
        }
        // insert checkpoints before for loop
        for (Map.Entry<Integer, Pair<String, Integer>> entry : intVariables.entrySet()) {
            sf = sf.insertStatement(blockId, sid - 1, constructCheckpointStatement(cid++, entry.getValue().getKey()));
        }

        System.out.println(sf);
    }

    private Statement constructCheckpointStatement(int id, String vName) {
        String s = String.format("gin.checkpoints.Checkpoint.log(%d, \"%s\", %s);", id, vName, vName);
        return parseStatement(s);
    }


    public static void main(String[] args) throws FileNotFoundException {
        new CheckpointsGenerator().run();
    }

}
