package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

/**
 * Represents a strategy that takes into account only the latest prices of the asset and their trend.
 */
public class MomentumInvestmentStrategy extends InvestmentStrategy {
    /**
     * Over how long period the analysis takes place (in days).
     */
    private int periodOfInterest;

    /**
     * @param assetManager reference to the asset manager.
     * @param period Over how long period the analysis takes place (in days).
     */
    public MomentumInvestmentStrategy(AssetManager assetManager, int period) {
        super(assetManager);
        this.periodOfInterest = period;
    }

    /**
     * Predicts the next price based on how much the price has changed over the last day.
     * @param assetType type of the asset of which price to predict.
     * @return predicted next price in the DEFAULT STANDARD CURRENCY per 1 unit of asset.
     */
    private double predictNextPrice(String assetType) {
        var history = this.assetManager.getAssetData(assetType).getPriceHistory();
        var lastPrice = history.get(history.size() - 1);
        var changeInValue = history.size() == 1 ? 0 : lastPrice - history.get(history.size() - 2);
        return Math.max(lastPrice + changeInValue, 0.001);
    }

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy returns the asset with the highest increase over the period.
     * @param availableAssets set of available assets.
     * @return
     */
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

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy returns the predicted price in the next day.
     * @param chosenAsset asset for which to find the price.
     * @return
     */
    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return this.predictNextPrice(chosenAsset);
    }

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy increases the price by 5%.
     * @param oldPrice old price of the offer in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     * @param amount amount of an asset in the offer.
     * @param availableFunds investment budget of the entity.
     * @param assetType asset of the offer.
     * @return
     */
    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        var newPrice = oldPrice * 1.05;
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds / amount;
        }
        return newPrice;
    }

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy returns an asset with the highest decrease over the period.
     * @param ownedAssets assets from which to choose one for sale.
     * @return
     */
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

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy returns the predicted price in the next day.
     * @param chosenAsset asset to sell.
     * @return
     */
    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return this.predictNextPrice(chosenAsset);
    }

    /**
     * {@inheritDoc}
     * In case of Momentum Investment Strategy lowers the price by 5%.
     * @param oldPrice old price of the offer per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param assetType asset the offer concerns.
     * @return
     */
    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * 0.95;
    }
}
