package simulation.market;

import simulation.asset.AssetManager;
import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class CommoditiesMarket extends Market{
    private HashSet<String> commodities;

    CommoditiesMarket(String name, double buyFee, double sellFee) throws IOException {
        super(name + " commodity", buyFee, sellFee);
        this.commodities = new HashSet<String>();
        this.initializeMarket();
    }

    @Override
    public void initializeMarket() {
        var manager = AssetManager.getInstance();
        var newCurrency = manager.createCurrencyAsset("US Dollar", 1.00, new String[]{"United States"});
        this.commodities.add(newCurrency.getUniqueIndetifyingName());
    }

    @Override
    public void addBuyOffer(String assetType, AssetHolder sender, double price, double size) {

    }

    @Override
    public void addSellOffer(String assetType, AssetHolder sender, double price, double size) {

    }

    @Override
    void processOffer(BuyOffer buyOffer, SellOffer sellOffer) {

    }

    @Override
    public void processAllOffers() {

    }

    @Override
    public ArrayList<String> getAvailableAssetTypes() {
        return null;
    }
}
