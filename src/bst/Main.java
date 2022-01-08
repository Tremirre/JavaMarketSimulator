package bst;

import simulation.Simulation;
import simulation.util.Constants;
import simulation.util.DataExporter;

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
        dataEx.exportPrices(simulation.getAssetManager().getAllAssetsPriceHistory());
    }
}