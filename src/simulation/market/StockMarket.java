package simulation.market;

import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.ArrayList;

public class StockMarket extends Market{
    private String traidingCurrency;
    private ArrayList<StockMarketIndex> stockMarketIndexes;

    public StockMarket(String name, double buyFee, double sellFee, String currency) throws IOException {
        super(name + " Stock", buyFee, sellFee);
        this.traidingCurrency = currency;
        this.stockMarketIndexes = new ArrayList<>();
    }

    @Override
    public void initializeMarket() {

    }

    @Override
    public String[] getAvailableAssetTypes() {
        return null;
    }
}
