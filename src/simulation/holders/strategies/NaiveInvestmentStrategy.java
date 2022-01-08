package simulation.holders.strategies;

import simulation.asset.AssetManager;
import simulation.util.RandomService;

import java.util.Set;

public class NaiveInvestmentStrategy extends InvestmentStrategy {
    private double riskFactor;

    public NaiveInvestmentStrategy(AssetManager assetManager, double riskFactor) {
        super(assetManager);
        this.riskFactor = riskFactor;
    }

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return (String) RandomService.getInstance().sampleElement(availableAssets.toArray());
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return this.assetManager.getAssetData(chosenAsset).getOpeningPrice() * this.riskFactor;
    }

    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
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
        return this.assetManager.getAssetData(chosenAsset).getOpeningPrice() * (2 - this.riskFactor);
    }

    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * this.riskFactor;
    }
}
