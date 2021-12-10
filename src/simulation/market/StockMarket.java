package simulation.market;

import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.ArrayList;

public class StockMarket extends Market{
    private String traidingCurrency;
    private ArrayList<StockMarketIndex> stockMarketIndexes;

    StockMarket(String name, double buyFee, double sellFee) throws IOException {
        super(name + " stock", buyFee, sellFee);
    }

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
