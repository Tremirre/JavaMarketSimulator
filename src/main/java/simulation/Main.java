package simulation;

import simulation.core.Simulation;
import simulation.util.Constants;
import simulation.util.DataExporter;

public class Main {
    public static void main(String[] args) {
        var simulation = new Simulation();
        simulation.start();
        var start = System.nanoTime();
        for (int i = 0; i < 2 * Constants.YEAR; i++) {
            System.out.println("Processing day no. " + i);
            simulation.runSimulationDay();
        }
        simulation.stop();
        System.out.println("Simulation took " + (System.nanoTime() - start) / 1_000_000 + "ms");
        DataExporter dataEx = new DataExporter();
        var prices = simulation.getAssetManager().getAllAssetsPriceHistory();
        dataEx.exportLabeledData(prices, "price_history.csv");
    }
}