package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

public class PassiveCompanyStrategy implements InvestmentStrategy{

    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return null;
    }

    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return 1.2 * AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice();
    }

    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        if (ownedAssets.size() > 1) {
            throw new RuntimeException("COMPANY HAS MORE THAN ONE ASSOSIATED ASSET!");
        }
        return ownedAssets.toArray(new String[0])[0];
    }

    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return 0.8 * AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice();
    }
}
