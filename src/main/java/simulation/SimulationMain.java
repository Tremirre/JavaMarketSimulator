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
     * This function runs a simulation with random setup of three different markets for 730 simulation days (buy default)
     * or other number if it was passed as an argument.
     * Outputs a csv file with price history of all assets that took a part in the simulation.
     */
    public static void main(String[] args) {
        int simLength = 2 * Constants.YEAR;
        if (args.length > 1) {
            try {
                simLength = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e) {
                System.out.println("Incorrect parameter passed as a number of days of the simulation!: " + args[1]);
                System.out.println("Simulation will close.");
                System.exit(1);
            }
        }
        var simulation = new Simulation();
        simulation.setupRandomMarket(AssetCategory.STOCK);
        simulation.setupRandomMarket(AssetCategory.CURRENCY);
        simulation.setupRandomMarket(AssetCategory.COMMODITY);
        simulation.getEntitiesManager().autoCreateInvestors();
        simulation.start();
        var start = System.nanoTime();
        System.out.println("Starting Simulation...");
        for (int i = 0; i < simLength; i++) {
            System.out.print("\rProcessing day no. " + i);
            simulation.runSimulationDay();
        }
        simulation.stop();
        System.out.println("\rSimulation took " + (System.nanoTime() - start) / 1_000_000 + "ms");
        DataExporter dataEx = new DataExporter();
        var prices = simulation.getAssetManager().getAllAssetsPriceHistory();
        dataEx.exportLabeledData(prices, "price_history.csv");
        System.out.println("Price history exported to ./exports/price_history.csv");
    }
}