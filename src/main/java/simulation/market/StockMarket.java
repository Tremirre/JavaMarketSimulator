package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.StockData;
import simulation.address.Address;

import java.util.HashSet;

public class StockMarket extends Market {
    private String tradingCurrency;
    final private HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();

    public StockMarket(AssetManager assetManager, String name, double buyFee, double sellFee, String currency, Address address) {
        super(assetManager, name + " Stock", buyFee, sellFee, address);
        this.tradingCurrency = currency;
    }

    public void addStockMarketIndex(StockMarketIndex idx) {
        this.stockMarketIndexes.add(idx);
        for (var company : idx.getCompanies()) {
            this.addNewAsset(company.getAssociatedAsset());
            company.sellAllStocks(this);
        }
    }

    public void addNewAsset(String stock) {
        if (!this.assetManager.doesAssetExist(stock, AssetCategory.STOCK))
            throw new IllegalArgumentException("Invalid asset type passed to a stock market: " + stock);
        this.assetTypesOnMarket.add(stock);
    }

    @Override
    protected void useTransactionData(String assetType, double price, double amount) {
        super.useTransactionData(assetType, price, amount);
        var stockData = (StockData) this.assetManager.getAssetData(assetType);
        var associatedCompany = stockData.getCompany();
        associatedCompany.recordTransactionData(price, amount);
    }

    public String getAssetTradingCurrency(String assetType) {
        return this.tradingCurrency;
    }

    public void setMarketCurrency(String currency) {
        if (this.assetManager.doesAssetExist(currency, AssetCategory.CURRENCY)) {
            throw new IllegalArgumentException("Non-existent currency passed as a market currency: " + currency);
        }
        this.tradingCurrency = currency;
    }

    public HashSet<StockMarketIndex> getStockMarketIndexes() {
        return this.stockMarketIndexes;
    }
}
