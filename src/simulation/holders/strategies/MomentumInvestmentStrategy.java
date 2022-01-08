package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

public class MomentumInvestmentStrategy extends InvestmentStrategy {
    private int periodOfInterest;

    public MomentumInvestmentStrategy(AssetManager assetManager, int period) {
        super(assetManager);
        this.periodOfInterest = period;
    }

    private double predictNextPrice(String assetType) {
        var history = this.assetManager.getAssetData(assetType).getPriceHistory();
        var lastPrice = history.get(history.size() - 1);
        var changeInValue = history.size() == 1 ? 0 : lastPrice - history.get(history.size() - 2);
        return Math.max(lastPrice + changeInValue, 0.001);
    }

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        String bestAsset = null;
        double highestIncreaseOverThePeriod = 0;
        for (var asset : availableAssets) {
            var history = this.assetManager.getAssetData(asset).getPriceHistory();
            if (history.size() < this.periodOfInterest) {
                continue;
            }
            double current = history.get(history.size() - 1);
            double previous = history.get(history.size() - this.periodOfInterest);
            if (current - previous >= highestIncreaseOverThePeriod) {
                highestIncreaseOverThePeriod = current - previous;
                bestAsset = asset;
            }
        }
        return bestAsset;
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return this.predictNextPrice(chosenAsset);
    }

    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        var newPrice = oldPrice * 1.05;
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds/amount;
        }
        return newPrice;
    }

    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        String bestAsset = null;
        double highestDecreaseOverThePeriod = 0;
        for (var asset : ownedAssets) {
            var history = this.assetManager.getAssetData(asset).getPriceHistory();
            if (history.size() < this.periodOfInterest) {
                continue;
            }
            double current = history.get(history.size() - 1);
            double previous = history.get(history.size() - this.periodOfInterest);
            if (current - previous <= highestDecreaseOverThePeriod) {
                highestDecreaseOverThePeriod = current - previous;
                bestAsset = asset;
            }
        }
        return bestAsset;
    }

    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return this.predictNextPrice(chosenAsset);
    }

    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * 0.95;
    }
}
