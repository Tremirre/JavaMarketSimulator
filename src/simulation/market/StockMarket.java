package simulation.market;

import simulation.holders.CompaniesManager;
import simulation.util.RandomService;

import java.util.ArrayList;
import java.util.HashSet;

public class StockMarket extends Market{
    private String traidingCurrency;
    private HashSet<StockMarketIndex> stockMarketIndexes;

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
        this.stockMarketIndexes = new HashSet<>();
        var idx = new StockMarketIndex();
        var initialNumberOfCompanies = RandomService.getInstance().yieldRandomNumber(5) + 1;
        for (int i = 0; i < initialNumberOfCompanies; i++) {
            idx.addCompany(CompaniesManager.getInstance().createNewCompany());
        }
        this.addStockMarketIndex(idx);
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
