package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

/**
 * This class represents the strategy that chooses and determines the price of the asset according
 * to the quality of that asset (based on asset's quality measure).
 */
public class QualitativeAssessmentStrategy extends InvestmentStrategy {
    /**
     * Factor that influences the final price of buy/sell offer recommended by the strategy.
     */
    private double individualAssessmentFactor;

    /**
     * @param assetManager reference to the asset manager.
     * @param factor factor influencing the final price of buy/sell offer recommended by the strategy.
     *               (As a multiplicand of final price).
     */
    public QualitativeAssessmentStrategy(AssetManager assetManager, double factor) {
        super(assetManager);
        this.individualAssessmentFactor = factor;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy it picks the asset with the highest quality measure.
     * @param availableAssets set of available assets.
     * @return
     */
    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        double bestQuality = 0;
        String bestAsset = null;
        for (var asset : availableAssets) {
            double currentQuality = this.assetManager.getAssetData(asset).getQualityMeasure();
            if (currentQuality >= bestQuality) {
                bestQuality = currentQuality;
                bestAsset = asset;
            }
        }
        return bestAsset;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy it returns a price below or equal to the latest asset price
     * dependently on the asset quality and the individual assessment factor.
     * @param chosenAsset asset for which to find the price.
     * @return
     */
    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        var assetData = this.assetManager.getAssetData(chosenAsset);
        var latestPrice = assetData.getOpeningPrice();
        var quality = assetData.getQualityMeasure();
        return (latestPrice * (quality / 4 + 0.75)) * this.individualAssessmentFactor;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy modifies the price taking into account the quality of the asset.
     * @param oldPrice old price of the offer in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     * @param amount amount of an asset in the offer.
     * @param availableFunds investment budget of the entity.
     * @param assetType asset of the offer.
     * @return
     */
    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        double newPrice = oldPrice * (1 + this.assetManager.getAssetData(assetType).getQualityMeasure() / 4);
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds / amount;
        }
        return newPrice;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy it chooses an asset with the lowest quality.
     * @param ownedAssets assets from which to choose one for sale.
     * @return
     */
    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        double worstQuality = 1;
        String worstAsset = null;
        for (var asset : ownedAssets) {
            double currentQuality = this.assetManager.getAssetData(asset).getQualityMeasure();
            if (currentQuality <= worstQuality) {
                worstQuality = currentQuality;
                worstAsset = asset;
            }
        }
        return worstAsset;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy returns a price higher or equal to the latest asset price
     * dependently on the asset quality and the individual assessment factor.
     * @param chosenAsset asset to sell.
     * @return
     */
    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        var assetData = this.assetManager.getAssetData(chosenAsset);
        var latestPrice = assetData.getOpeningPrice();
        var quality = assetData.getQualityMeasure();
        return latestPrice * (1 + quality / 4) * this.individualAssessmentFactor;
    }

    /**
     * {@inheritDoc}
     * In case of the Qualitative Assessment Strategy modifies the price taking into account the asset's quality measure.
     * @param oldPrice old price of the offer per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param assetType asset the offer concerns.
     * @return
     */
    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * (this.assetManager.getAssetData(assetType).getQualityMeasure() / 4 + 0.75);
    }
}
