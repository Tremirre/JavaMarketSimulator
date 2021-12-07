package simulation.market;

import simulation.asset.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.util.ArrayList;

public class StockMarket extends Market{
    private String traidingCurrency;
    private ArrayList<StockMarketIndex> stockMarketIndexes;

    @Override
    public void initializeMarket() {

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
