package simulation.holders.strategies;

import simulation.asset.AssetManager;
import simulation.util.RandomService;

import java.util.Set;

public interface InvestmentStrategy {
    String chooseAssetToBuy(Set<String> availableAssets);
    double determineOptimalBuyingPrice(String chosenAsset);
    default double determineOptimalBuyingSize(String chosenAsset, double price, double availableFunds) {
        var rand = RandomService.getInstance();
        double amount;
        if (!AssetManager.getInstance().getAssetData(chosenAsset).isSplittable()) {
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
    default double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {return oldPrice;}

    String chooseAssetToSell(Set<String> ownedAssets);
    double determineOptimalSellingPrice(String chosenAsset);
    default double determineOptimalSellingSize(String chosenAsset, double availableAmount) {
        return availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
    }
    default double updateSellPrice(double oldPrice, String assetType) {return oldPrice;}
}
