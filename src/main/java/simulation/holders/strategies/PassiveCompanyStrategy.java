package simulation.holders.strategies;

import simulation.asset.AssetManager;

import java.util.Set;

/**
 * Represents a strategy of an entity that does not take active part in trading.
 * It strives to sell everything the entity has and sends buy offers only on user's request (on buyout event).
 * This strategy does not change the offer's price over time.
 */
public class PassiveCompanyStrategy extends InvestmentStrategy {

    public PassiveCompanyStrategy(AssetManager assetManager) {
        super(assetManager);
    }

    /**
     * {@inheritDoc}
     * In case of Passive Company Strategy it chooses the first asset on the list.
     * @param availableAssets set of available assets.
     * @return
     */
    @Override
    public String chooseAssetToBuy(Set<String> availableAssets) {
        return availableAssets.toArray(new String[0])[0];
    }

    /**
     * {@inheritDoc}
     * In case of Passive Company Strategy it sends price that is 120% of the latest price of the asset,
     * hence making the entity's offer more attractive.
     * @param chosenAsset asset for which to find the price.
     * @return
     */
    @Override
    public double determineOptimalBuyingPrice(String chosenAsset) {
        return 1.2 * this.assetManager.getAssetData(chosenAsset).getOpeningPrice();
    }

    /**
     * {@inheritDoc}
     * In case of Passive Company Strategy it chooses the first asset on the list.
     * @param ownedAssets assets from which to choose one for sale.
     * @return
     */
    @Override
    public String chooseAssetToSell(Set<String> ownedAssets) {
        return ownedAssets.toArray(new String[0])[0];
    }

    /**
     * {@inheritDoc}
     * In case of Passive Company Strategy it sends price that is 80% of the latest price of the asset,
     * hence making the entity's offer more attractive.
     * @param chosenAsset asset to sell.
     * @return
     */
    @Override
    public double determineOptimalSellingPrice(String chosenAsset) {
        return 0.8 * this.assetManager.getAssetData(chosenAsset).getOpeningPrice();
    }

    /**
     * {@inheritDoc}
     * In case of Passive Company Strategy it returns the maximal amount.
     * @param chosenAsset asset chosen for sale.
     * @param availableAmount maximal amount that can be sold.
     * @return
     */
    @Override
    public double determineOptimalSellingSize(String chosenAsset, double availableAmount) {
        return availableAmount;
    }
}
