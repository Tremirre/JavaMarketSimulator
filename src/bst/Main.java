package bst;

import simulation.Simulation;
import simulation.asset.AssetManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var simulation = new Simulation();
        String stock = simulation.companies.get(0).getAssociatedAsset();
        System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestSellingPrice());
        for (int i = 0; i < 10; i++) {
            System.out.println("Simulation day no. " + (i + 1));
            System.out.println(AssetManager.getInstance().getAssetData(stock).getLatestSellingPrice());
            simulation.runSimulationDay();
        }
    }
}