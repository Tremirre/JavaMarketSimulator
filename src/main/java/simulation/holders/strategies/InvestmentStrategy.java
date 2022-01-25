package simulation.holders.strategies;

import simulation.asset.AssetManager;
import simulation.util.RandomService;

import java.util.Set;

/**
 * Base class representing an Investment Strategy.
 */
public abstract class InvestmentStrategy {
    /**
     * Reference to the asset manager.
     */
    protected AssetManager assetManager;

    /**
     * @param assetManager reference to the asset manager.
     */
    InvestmentStrategy(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Chooses which asset to buy from the set of assets.
     * @param availableAssets set of available assets.
     * @return chosen asset to buy.
     */
    public abstract String chooseAssetToBuy(Set<String> availableAssets);

    /**
     * Based on the chosen asset determines an optimal price.
     * @param chosenAsset asset for which to find the price.
     * @return optimal price per 1 unit of the asset in DEFAULT STANDARD CURRENCY
     */
    public abstract double determineOptimalBuyingPrice(String chosenAsset);

    /**
     * Determines how much of asset to buy given the calculated optimal price, asset to buy and available funds.
     * If the amount is lesser than 0.1, the asset will not be bought.
     * @param chosenAsset asset for which to find amount to buy.
     * @param price obtained optimal price of the asset.
     * @param availableFunds investment budget of the entity.
     * @return optimal amount of the asset to buy.
     */
    public double determineOptimalBuyingSize(String chosenAsset, double price, double availableFunds) {
        var rand = RandomService.getInstance();
        double amount;
        if (!this.assetManager.getAssetData(chosenAsset).isSplittable()) {
            amount = rand.yieldRandomInteger(5);
            while (availableFunds < price * amount && amount > 0) {
                amount--;
            }
        } else {
            amount = rand.yieldRandomNumber(4.5) + 0.5;
            while (availableFunds < price * amount && amount > 0.1) {
                amount /= 2;
            }
        }
        return amount < 0.1 ? 0 : amount;
    }

    /**
     * Given old price, amount of the asset in the buy offer, available funds of the entity and the asset which the
     * offer concerns, returns an updated price of the offer.
     * @param oldPrice old price of the offer in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     * @param amount amount of an asset in the offer.
     * @param availableFunds investment budget of the entity.
     * @param assetType asset of the offer.
     * @return updated price of the offer in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     */
    public double updateBuyPrice(double oldPrice, double amount, double availableFunds, String assetType) {
        return oldPrice;
    }

    /**
     * Chooses which asset to sell from the passed set of assets.
     * @param ownedAssets assets from which to choose one for sale.
     * @return chosen asset to sell.
     */
    public abstract String chooseAssetToSell(Set<String> ownedAssets);

    /**
     * Given the asset, determines an optimal selling price.
     * @param chosenAsset asset to sell.
     * @return selling price of an asset in DEFAULT STANDARD CURRENCY per 1 unit of the asset.
     */
    public abstract double determineOptimalSellingPrice(String chosenAsset);

    /**
     * Determines how much of the chosen assets to sell, given the available amount of that asset.
     * @param chosenAsset asset chosen for sale.
     * @param availableAmount maximal amount that can be sold.
     * @return optimal amount of an asset to sell.
     */
    public double determineOptimalSellingSize(String chosenAsset, double availableAmount) {
        return availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
    }

    /**
     * Given the asset and its old price, returns an updated price of a sell offer.
     * @param oldPrice old price of the offer per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param assetType asset the offer concerns.
     * @return updated price.
     */
    public double updateSellPrice(double oldPrice, String assetType) {
        return oldPrice;
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }
}
