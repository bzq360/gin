package gin.checkpoints;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.metamodel.IntegerLiteralExprMetaModel;
import com.opencsv.CSVWriter;
import gin.Patch;
import gin.SourceFileTree;
import javafx.util.Pair;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static com.github.javaparser.JavaParser.parseStatement;

public class CheckpointUtils {

    /*============== logging checkpoint  ==============*/

    private static int numOfCheckpoints;

    private static List<Checkpoint> checkpointsLst;

    private static List<Checkpoint> referenceCheckpointsLst;

    private static CSVWriter csvWriter;

    private static int checkpointCounter = 0;

    private static boolean write;

    public static void setup(boolean writeToFile) {
        write = writeToFile;
        // setup csv writer (optional)
        if (write) {
            try {
                String fname = "checkpoint_logs/checkpoints_" + checkpointCounter + ".csv";
                File file = new File(fname);
                Files.deleteIfExists(file.toPath());
                csvWriter = new CSVWriter(new FileWriter(new File(fname), false));
                String[] header = {"checkpointID", "variable name", "value"};
                csvWriter.writeNext(header);
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkpointCounter++;
        // setup list
        checkpointsLst = new ArrayList<>();
    }

    public static void setupOriginalCheckpoints() {
        referenceCheckpointsLst = new ArrayList<>(checkpointsLst);
        checkpointsLst = new ArrayList<>(); // reset list
    }

    public static void log(int cid, String vName, int value) {
        // log in file (optional)
        if (write) {
            String[] entry = {cid + "", vName, value + ""};
            try {
                csvWriter.writeNext(entry);
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // log in list
        checkpointsLst.add(new Checkpoint(cid, vName, value));
    }

    /*============== inserting checkpoints  ==============*/

    public static Patch insertCheckpoints(Patch orig) {
        Map<Integer, AbstractMap.SimpleEntry<String, Integer>> intVariables = new HashMap<>(); // <node id, (variable name, value)>

        SourceFileTree sf = (SourceFileTree) orig.getSourceFile().copyOf();

        System.out.println("before: -----------------------------------------------");
        System.out.println(sf);

        // store integer variable declarations
        List<Integer> intLst = sf.getNodeIDsByClass(true, VariableDeclarationExpr.class);
        for (int nid : intLst) {
            VariableDeclarationExpr ve = (VariableDeclarationExpr) sf.getNode(nid);

            // jump over non-int variables
            if (!ve.getChildNodes().get(0).getChildNodes().get(0).equals(PrimitiveType.intType()))
                continue;

            // jump over variables inside foreach loop
            if (sf.getOriginalNode(nid).findAncestor(ForeachStmt.class).isPresent())
                continue;

            VariableDeclarator vd = (VariableDeclarator) ve.getChildNodes().get(0);
            String vName = vd.getNameAsString();
            for (Node node : vd.getChildNodes()) {
                if (node.getMetaModel() instanceof IntegerLiteralExprMetaModel) {
                    int value = ((IntegerLiteralExpr) node).asInt();
                    intVariables.put(nid, new AbstractMap.SimpleEntry<>(vName, value));
                }
            }
        }

        // assume we have only 1 foreach stmt in this case
        List<Integer> forLst = sf.getNodeIDsByClass(true, ForeachStmt.class);
        int sid = forLst.get(0);
        int blockId = sf.getBlockIDsInTargetMethod().get(0);

        int cid = 0; // checkpoint id
        // insert checkpoints after for loop
        for (Map.Entry<Integer, AbstractMap.SimpleEntry<String, Integer>> entry : intVariables.entrySet()) {
            sf = sf.insertStatement(blockId, sid, constructCheckpointStatement(cid++, entry.getValue().getKey()));
        }
        // insert checkpoints before for loop
        for (Map.Entry<Integer, AbstractMap.SimpleEntry<String, Integer>> entry : intVariables.entrySet()) {
            sf = sf.insertStatement(blockId, sid - 1, constructCheckpointStatement(cid++, entry.getValue().getKey()));
        }
        numOfCheckpoints = cid;

//        System.out.println("after: -----------------------------------------------");
//        System.out.println(sf);



        return new Patch(sf);
    }

    private static Statement constructCheckpointStatement(int id, String vName) {
        String s = String.format("gin.checkpoints.CheckpointUtils.log(%d, \"%s\", %s);", id, vName, vName);
        return parseStatement(s);
    }

    /*============== calculate distances between checkpoints  ==============*/

    // compute distance between checkpointLst and referenceCheckpointLst
    // goal is to maximum the fitness score
    public static double computeDistance(int testCaseID, boolean origPass, boolean currPass) {
        double score = 0;

        if (checkpointsLst.size() != referenceCheckpointsLst.size()) {
            Logger.error("the number of checkpoints between reference and current programs are different!");
        }

        int startIndex = testCaseID * numOfCheckpoints;
        for (int i = startIndex; i < startIndex + numOfCheckpoints; i++) {
            int curr = checkpointsLst.get(i).value;
            int ref = referenceCheckpointsLst.get(i).value;
            double distance = Math.abs(curr - ref);
            distance = normalize(distance);
            score += (1 - distance);
        }
//        System.out.println("* distance = " + score); // TODO: delete

        return score;
    }


    private static double normalize(double x) {
        return x / (x + 1);
    }

}
