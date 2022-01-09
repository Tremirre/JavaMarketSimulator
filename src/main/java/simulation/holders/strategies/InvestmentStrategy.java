package main.java.simulation.holders.strategies;

import main.java.simulation.asset.AssetManager;
import main.java.simulation.util.RandomService;

import java.util.Set;

public abstract class InvestmentStrategy {
    protected AssetManager assetManager;

    InvestmentStrategy(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public abstract String chooseAssetToBuy(Set<String> availableAssets);

    public abstract double determineOptimalBuyingPrice(String chosenAsset);

    public double determineOptimalBuyingSize(String chosenAsset, double price, double availableFunds) {
        var rand = RandomService.getInstance();
        double amount;
        if (!this.assetManager.getAssetData(chosenAsset).isSplittable()) {
            amount = rand.yieldRandomInteger(5);
            while (availableFunds < price * amount && amount > 0) {
                amount--;
            }
        } else {
            amount = rand.yieldRandomNumber(4.5) + 0.5;
            while (availableFunds < price * amount && amount > 0.1) {
                amount/=2;
            }
        }
        return amount < 0.25 ? 0 : amount;
    }

    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {return oldPrice;}

    public abstract String chooseAssetToSell(Set<String> ownedAssets);

    public abstract double determineOptimalSellingPrice(String chosenAsset);

    public double determineOptimalSellingSize(String chosenAsset, double availableAmount) {
        return availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
    }

    public double updateSellPrice(double oldPrice, String assetType) {return oldPrice;}

    public AssetManager getAssetManager() { return this.assetManager; }
}
