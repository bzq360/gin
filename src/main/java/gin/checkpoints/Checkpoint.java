package gin.checkpoints;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Checkpoint {

    private static CSVWriter outputFileWriter = null;

    static {
        try {
            outputFileWriter = new CSVWriter(new FileWriter(new File("checkpoints.csv")));
            String[] header = {"checkpointID", "variable name", "value"};
            outputFileWriter.writeNext(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(int cid, String vName, int value) {
        String[] entry = {cid + "", vName, value + ""};
        outputFileWriter.writeNext(entry);
    }

}
