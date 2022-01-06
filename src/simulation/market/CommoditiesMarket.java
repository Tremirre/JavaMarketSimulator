package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.CommodityData;
import simulation.asset.RandomSupplementaryAssetFactory;
import simulation.util.RandomService;

import java.util.HashSet;

public class CommoditiesMarket extends Market{
    private HashSet<String> commodities;
    private InitialVoidSeller ivs;

    public CommoditiesMarket(String name, double buyFee, double sellFee) {
        super(name + " Commodity", buyFee, sellFee);
    }

    @Override
    public void initializeMarket() {
        this.commodities = new HashSet<>();
        this.ivs = new InitialVoidSeller();
        var initialNumberOfCommodities = RandomService.getInstance().yieldRandomInteger(5) + 1;
        for (int i = 0; i< initialNumberOfCommodities; i++) {
            var newCommodity = new RandomSupplementaryAssetFactory().createCommodityAsset();
            this.addNewCommodity(newCommodity);
        }
    }

    @Override
    public synchronized HashSet<String> getAvailableAssetTypes() {
        return this.commodities;
    }

    public void addNewCommodity(String commodity) {
        this.commodities.add(commodity);
        this.ivs.sendInitialOffer(this, commodity);
    }

    public String getAssetTradingCurrency(String commodity) {
        var assetManager = AssetManager.getInstance();
        if (!assetManager.doesAssetExist(commodity, AssetCategory.COMMODITY)) {
             throw new RuntimeException("INVALID COMMODITY SENT TO THE MARKET: " + commodity);
        }
        return ((CommodityData) assetManager.getAssetData(commodity)).getTradingCurrency();
    }
}
