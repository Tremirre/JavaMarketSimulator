package simulation.holders.strategies;

import simulation.asset.AssetManager;
import simulation.util.RandomService;

import java.util.Set;

public class NaiveInvestmentStrategy implements InvestmentStrategy {
    private double riskFactor;

    public NaiveInvestmentStrategy(double riskFactor) {
        this.riskFactor = riskFactor;
    }

    public NaiveInvestmentStrategy() {
        this(0.95);
    }

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return (String) RandomService.getInstance().sampleElement(availableAssets.toArray());
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice() * this.riskFactor;
    }

    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds) {
        double newPrice = oldPrice * (2 - this.riskFactor);
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds/amount;
        }
        return newPrice;
    }

    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        return (String) RandomService.getInstance().sampleElement(ownedAssets.toArray());
    }

    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice() * (2 - this.riskFactor);
    }

    @Override
    public double updateSellPrice(double oldPrice) {
        return oldPrice * this.riskFactor;
    }
}
