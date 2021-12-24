package bst;

import simulation.Simulation;
import simulation.asset.AssetManager;

public class Main {

    public static void main(String[] args) {
        var simulation = new Simulation();
        String stock = simulation.companies.get(0).getAssociatedAsset();
        System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestAverageSellingPrice());
        var start = System.nanoTime();
        for (int i = 0; i < 2*365; i++) {
            //System.out.println("Simulation day no. " + (i + 1));
            System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestAverageSellingPrice());
            try {
                simulation.runSimulationDay();
            } catch(InterruptedException e) {
                break;
            }
        }
        simulation.stopSimulation();
        System.out.println("Simulation took " + (System.nanoTime() - start)/1_000_000 + "ms");
    }
}