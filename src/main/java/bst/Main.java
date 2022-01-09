package main.java.bst;

import main.java.simulation.core.Simulation;
import main.java.simulation.util.Constants;
import main.java.simulation.util.DataExporter;

public class Main {
    public static void main(String[] args) {
        var simulation = new Simulation();
        var start = System.nanoTime();
        for (int i = 0; i < 2 * Constants.YEAR; i++) {
            System.out.println("Processing day no. " + i);
            simulation.runSimulationDay(i);
        }
        simulation.stop();
        System.out.println("Simulation took " + (System.nanoTime() - start)/1_000_000 + "ms");
        DataExporter dataEx = new DataExporter();
        var prices = simulation.getAssetManager().getAllAssetsPriceHistory();
        dataEx.exportLabeledData(prices, "price_history.csv");
    }
}