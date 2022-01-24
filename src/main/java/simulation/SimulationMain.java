package simulation;

import simulation.asset.AssetCategory;
import simulation.core.Simulation;
import simulation.util.Constants;
import simulation.util.DataExporter;

/**
 * Driver class for the console application, used for prototyping and testing.
 */
public class SimulationMain {
    /**
     * This function runs a simulation with random setup of three different markets for 2 simulation years, outputs
     * a csv file with price history of all assets that took a part in the simulation.
     * @parameter args list of arguments passed to the application when starting it from the console. (Currently ignored)
     */
    public static void main(String[] args) {
        var simulation = new Simulation();
        simulation.setupRandomMarket(AssetCategory.STOCK);
        simulation.setupRandomMarket(AssetCategory.CURRENCY);
        simulation.setupRandomMarket(AssetCategory.COMMODITY);
        simulation.getEntitiesManager().autoCreateInvestors();
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