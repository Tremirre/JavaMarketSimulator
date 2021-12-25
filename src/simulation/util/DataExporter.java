package simulation.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class DataExporter {
    public void exportPrices(HashMap<String, ArrayList<Double>> prices) {
        try (Writer writer = new FileWriter("price_history.csv")) {
            ArrayList<String> assetNames = new ArrayList<>(prices.keySet());
            StringBuilder line = new StringBuilder();
            int historyLength = 0;
            for (var assetName : assetNames) {
                if (prices.get(assetName).size() > historyLength)
                    historyLength = prices.get(assetName).size();
                line.append(assetName);
                line.append(';');
            }
            line.setLength(line.length() - 1);
            line.append(System.lineSeparator());
            writer.append(line.toString());
            line.setLength(0);
            for (int i = 0; i < historyLength; i++) {
                for (var assetName : assetNames) {
                    line.append(prices.get(assetName).get(i));
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
