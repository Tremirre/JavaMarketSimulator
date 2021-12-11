package simulation.market;

import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.ArrayList;

public class StockMarket extends Market{
    private String traidingCurrency;
    private ArrayList<StockMarketIndex> stockMarketIndexes;

    public StockMarket(String name, double buyFee, double sellFee) throws IOException {
        super(name + " Stock", buyFee, sellFee);
    }

    @Override
    public void initializeMarket() {

    }

    @Override
    void processOffer(BuyOffer buyOffer, SellOffer sellOffer) {

    }

    @Override
    public void processAllOffers() {

    }

    @Override
    public String[] getAvailableAssetTypes() {
        return null;
    }
}
