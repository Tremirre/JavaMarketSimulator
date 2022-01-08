package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

public class PassiveCompanyStrategy extends InvestmentStrategy{

    public PassiveCompanyStrategy(AssetManager assetManager) {
        super(assetManager);
    }

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return null;
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return 1.2 * this.assetManager.getAssetData(chosenAsset).getOpeningPrice();
    }

    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        if (ownedAssets.size() > 1) {
            throw new RuntimeException("COMPANY HAS MORE THAN ONE ASSOCIATED ASSET!");
        }
        return ownedAssets.toArray(new String[0])[0];
    }

    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return 0.8 * this.assetManager.getAssetData(chosenAsset).getOpeningPrice();
    }

    @Override
    public double determineOptimalSellingSize(String chosenAsset, double availableAmount) {
        return availableAmount;
    }
}
