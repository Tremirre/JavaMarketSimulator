package simulation.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataExporter {
    private void makeDirectory(String dirname) {
        File dir = new File(dirname);
        dir.mkdir();
    }

    public <T> void exportLabeledData(HashMap<String, ? extends List<T>> data, String exportedFileName) {
        var dirname = "exports";
        makeDirectory(dirname);
        try (Writer writer = new FileWriter(dirname + "\\" + exportedFileName)) {
            ArrayList<String> assetNames = new ArrayList<>(data.keySet());
            StringBuilder line = new StringBuilder();
            int historyLength = 0;
            for (var assetName : assetNames) {
                if (data.get(assetName).size() > historyLength)
                    historyLength = data.get(assetName).size();
                line.append(assetName);
                line.append(';');
            }
            line.setLength(line.length() - 1);
            line.append(System.lineSeparator());
            writer.append(line.toString());
            line.setLength(0);
            for (int i = 0; i < historyLength; i++) {
                for (var assetName : assetNames) {
                    line.append(data.get(assetName).get(i));
                    line.append(';');
                }
                line.setLength(line.length() - 1);
                line.append(System.lineSeparator());
                writer.append(line.toString());
                line.setLength(0);
            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
