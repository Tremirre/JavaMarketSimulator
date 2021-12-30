package simulation.market;

import java.util.ArrayList;
import java.util.HashSet;

public class StockMarket extends Market{
    private String traidingCurrency;
    private ArrayList<StockMarketIndex> stockMarketIndexes;

    public StockMarket(String name, double buyFee, double sellFee, String currency) {
        super(name + " Stock", buyFee, sellFee);
        this.traidingCurrency = currency;
    }

    public void addStockMarketIndex(StockMarketIndex idx) {
        this.stockMarketIndexes.add(idx);
        for (var company : idx.getCompanies())
            company.sendInitialOffer(this);
    }

    @Override
    public void initializeMarket() {
        this.stockMarketIndexes = new ArrayList<>();
    }

    @Override
    public synchronized HashSet<String> getAvailableAssetTypes() {
        HashSet<String> assets = new HashSet<>();
        for (var idx : this.stockMarketIndexes) {
            for (var company : idx.getCompanies()) {
                assets.add(company.getAssociatedAsset());
            }
        }
        return assets;
    }
}
