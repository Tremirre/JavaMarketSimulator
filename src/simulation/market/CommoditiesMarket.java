package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.CommodityData;

public class CommoditiesMarket extends Market{
    private InitialVoidSeller ivs = new InitialVoidSeller();

    public CommoditiesMarket(String name, double buyFee, double sellFee) {
        super(name + " Commodity", buyFee, sellFee);
    }

    public void addNewAsset(String commodity) {
        if (!AssetManager.getInstance().doesAssetExist(commodity, AssetCategory.COMMODITY))
            throw new IllegalArgumentException("Invalid asset type passed to a commodity market: " + commodity);
        this.assetTypesOnMarket.add(commodity);
        this.ivs.sendInitialOffer(this, commodity);
    }

    public String getAssetTradingCurrency(String commodity) {
        var assetManager = AssetManager.getInstance();
        if (!assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY)) {
             throw new IllegalArgumentException("Invalid asset type passed to a currency market: " + commodity);
        }
        return ((CommodityData) assetManager.getAssetData(commodity)).getTradingCurrency();
    }
}
