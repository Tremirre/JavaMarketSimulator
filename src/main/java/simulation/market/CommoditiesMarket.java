package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.CommodityData;
import simulation.address.Address;

/**
 * Class representing a commodities market.
 */
public class CommoditiesMarket extends Market {
    /**
     * Initial void seller tasked with sending initial offers for new assets.
     */
    private final InitialVoidSeller ivs = new InitialVoidSeller();

    /**
     * Constructor matching parent constructor.
     * Appends the provided prefix name by the type of the market.
     * @param assetManager AssetManager reference.
     * @param name Prefix of the name of the market.
     * @param buyFee Buy fee on the market.
     * @param sellFee Sell fee on the market.
     * @param address Address of the market.
     */
    public CommoditiesMarket(AssetManager assetManager, String name, double buyFee, double sellFee, Address address) {
        super(assetManager, name + " Commodity", buyFee, sellFee, address);
    }

    /**
     * Adds new commodity to the pool of available commodities on the market. Throws exception if non-existing commodity
     * identifier is passed. Calls initial void seller to send initial offer to the market.
     * @param commodity asset to be added to the market.
     */
    public void addNewAsset(String commodity) {
        if (!this.assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY))
            throw new IllegalArgumentException("Invalid asset type passed to a commodity market: " + commodity);
        this.assetTypesOnMarket.add(commodity);
        this.ivs.sendInitialOffer(this, commodity);
    }

    /**
     * Returns the trading currency of the commodity. Throws exception if non-existing commodity identifier is passed.
     * @param commodity queried asset.
     * @return trading currency of the commodity.
     */
    public String getAssetTradingCurrency(String commodity) {
        if (!this.assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY)) {
            throw new IllegalArgumentException("Invalid asset type passed to a currency market: " + commodity);
        }
        return ((CommodityData) this.assetManager.getAssetData(commodity)).getTradingCurrency();
    }
}
