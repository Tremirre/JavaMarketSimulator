package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.CommodityData;
import simulation.holders.Address;

public class CommoditiesMarket extends Market {
    private InitialVoidSeller ivs = new InitialVoidSeller();

    public CommoditiesMarket(AssetManager assetManager, String name, double buyFee, double sellFee, Address address) {
        super(assetManager, name + " Commodity", buyFee, sellFee, address);
    }

    public void addNewAsset(String commodity) {
        if (!this.assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY))
            throw new IllegalArgumentException("Invalid asset type passed to a commodity market: " + commodity);
        this.assetTypesOnMarket.add(commodity);
        this.ivs.sendInitialOffer(this, commodity);
    }

    public String getAssetTradingCurrency(String commodity) {
        if (!this.assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY)) {
            throw new IllegalArgumentException("Invalid asset type passed to a currency market: " + commodity);
        }
        return ((CommodityData) this.assetManager.getAssetData(commodity)).getTradingCurrency();
    }
}
