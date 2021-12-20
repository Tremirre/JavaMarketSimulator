package bst;

import simulation.Simulation;
import simulation.asset.AssetManager;
import simulation.util.DebugLogger;

public class Main {

    public static void main(String[] args) {
        DebugLogger.initLogger();
        var simulation = new Simulation();
        String stock = simulation.companies.get(0).getAssociatedAsset();
        System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestSellingPrice());
        var start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            DebugLogger.logMessage("Processing day no. " + (i + 1));
            System.out.println("Simulation day no. " + (i + 1));
            System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestSellingPrice());
            try {
                simulation.runSimulationDay();
            } catch(InterruptedException e) {
                break;
            }
        }
        DebugLogger.quitLogger();
        simulation.stopSimulation();
        System.out.println("Simulation took " + (System.nanoTime() - start)/1_000_000 + "ms");
    }
}