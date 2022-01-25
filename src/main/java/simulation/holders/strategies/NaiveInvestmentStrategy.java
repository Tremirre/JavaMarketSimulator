package simulation.holders.strategies;

import simulation.asset.AssetManager;
import simulation.util.RandomService;

import java.util.Set;

/**
 * This strategy represents a naive investor who chooses assets at random and determines the price
 * without any analysis of the asset, taking into account only its latest price.
 */
public class NaiveInvestmentStrategy extends InvestmentStrategy {
    /**
     * Factor that determines how much the investor will try to gain from an offer.
     * Ideally it should be in the range between 0.8 and 1.0.
     * The lower the factor, the more risky offer the entity will send.
     */
    private double riskFactor;

    /**
     * @param assetManager reference to the asset manager.
     * @param riskFactor risk factor of the strategy.
     */
    public NaiveInvestmentStrategy(AssetManager assetManager, double riskFactor) {
        super(assetManager);
        this.riskFactor = Math.min(Math.max(riskFactor, 0.1), 1);
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy it picks the asset at random.
     * @param availableAssets set of available assets.
     * @return
     */
    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return (String) RandomService.getInstance().sampleElement(availableAssets.toArray());
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy, it returns a price that is below the latest price of the asset.
     * @param chosenAsset asset for which to find the price.
     * @return
     */
    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return this.assetManager.getAssetData(chosenAsset).getOpeningPrice() * this.riskFactor;
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy it slightly increases the price (according to its risk factor).
     * @param oldPrice old price of the offer in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     * @param amount amount of an asset in the offer.
     * @param availableFunds investment budget of the entity.
     * @param assetType asset of the offer.
     * @return
     */
    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        double newPrice = oldPrice * (2 - this.riskFactor);
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds / amount;
        }
        return newPrice;
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy picks an asset at random.
     * @param ownedAssets assets from which to choose one for sale.
     * @return
     */
    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        return (String) RandomService.getInstance().sampleElement(ownedAssets.toArray());
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy, it returns a price that is higher that the latest price of the asset.
     * @param chosenAsset asset to sell.
     * @return
     */
    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return this.assetManager.getAssetData(chosenAsset).getOpeningPrice() * (2 - this.riskFactor);
    }

    /**
     * {@inheritDoc}
     * In case of Naive Investment Strategy it slightly decreases the price (according to its risk factor).
     * @param oldPrice old price of the offer per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param assetType asset the offer concerns.
     * @return
     */
    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * this.riskFactor;
    }
}
