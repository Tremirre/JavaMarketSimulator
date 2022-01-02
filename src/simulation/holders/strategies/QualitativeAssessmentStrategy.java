package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

public class QualitativeAssessmentStrategy implements InvestmentStrategy {
    private double individualAssessmentFactor;

    public QualitativeAssessmentStrategy(double factor) {
        this.individualAssessmentFactor = factor;
    }

    public QualitativeAssessmentStrategy() {
        this(1);
    }

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        double bestQuality = 0;
        String bestAsset = null;
        for (var asset : availableAssets) {
            double currentQuality = AssetManager.getInstance().getAssetData(asset).getQualityMeasure();
            if (currentQuality >= bestQuality) {
                bestQuality = currentQuality;
                bestAsset = asset;
            }
        }
        return bestAsset;
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        var assetData = AssetManager.getInstance().getAssetData(chosenAsset);
        var latestPrice = assetData.getLatestAverageSellingPrice();
        var quality = assetData.getQualityMeasure();
        return (latestPrice * (quality/4 + 0.75)) * this.individualAssessmentFactor;
    }

    @Override
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        double newPrice = oldPrice * (1 + AssetManager.getInstance().getAssetData(assetType).getQualityMeasure()/4);
        if (availableFunds < (newPrice - oldPrice) * amount) {
            newPrice = oldPrice + availableFunds/amount;
        }
        return newPrice;
    }

    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        double worstQuality = 1;
        String worstAsset = null;
        for (var asset : ownedAssets) {
            double currentQuality = AssetManager.getInstance().getAssetData(asset).getQualityMeasure();
            if (currentQuality <= worstQuality) {
                worstQuality = currentQuality;
                worstAsset = asset;
            }
        }
        return worstAsset;
    }

    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        var assetData = AssetManager.getInstance().getAssetData(chosenAsset);
        var latestPrice = assetData.getLatestAverageSellingPrice();
        var quality = assetData.getQualityMeasure();
        return latestPrice * (1 + quality/4) * this.individualAssessmentFactor;
    }

    @Override
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice * (AssetManager.getInstance().getAssetData(assetType).getQualityMeasure()/4 + 0.75);
    }
}
