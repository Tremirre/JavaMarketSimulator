package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.StockData;
import simulation.address.Address;
import simulation.util.Constants;

import java.util.HashSet;

/**
 * Class representing a stock market.
 */
public class StockMarket extends Market {
    /**
     * Trading currency of the market.
     */
    private String tradingCurrency;
    /**
     * Set of stock market indexes available on the market.
     */
    final private HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();

    /**
     * Constructor matching parent constructor.
     * Appends the provided prefix name by the type of the market.
     * @param assetManager AssetManager reference.
     * @param name Prefix of the name of the market.
     * @param buyFee Buy fee on the market.
     * @param sellFee Sell fee on the market.
     * @param currency trading currency of the market.
     * @param address Address of the market.
     */
    public StockMarket(AssetManager assetManager, String name, double buyFee, double sellFee, String currency, Address address) {
        super(assetManager, name + " Stock", buyFee, sellFee, address);
        this.tradingCurrency = currency;
    }

    /**
     * Adds new stock market index to the market, adds companies in the index to set of available assets for trading
     * on the market, calls the companies to sell all of their free stocks on the market.
     * @param idx stock market index to be added.
     */
    public void addStockMarketIndex(StockMarketIndex idx) {
        this.stockMarketIndexes.add(idx);
        for (var company : idx.getCompanies()) {
            this.addNewAsset(company.getAssociatedAsset());
            company.sellAllStocks(this);
        }
    }

    /**
     * Adds new stock asset to the market. Raises exception if it does not exist.
     * @param stock asset to be added.
     */
    public void addNewAsset(String stock) {
        if (!this.assetManager.doesAssetExist(stock, AssetCategory.STOCK))
            throw new IllegalArgumentException("Invalid asset type passed to a stock market: " + stock);
        this.assetTypesOnMarket.add(stock);
    }

    /**
     * Calls the parent method for using the transaction data.
     * Passes transaction data to the associated company of the asset.
     * @param assetType asset of the transaction.
     * @param price price of the transaction in DEFAULT STANDARD CURRENCY for 1 unit of the asset.
     * @param amount amount of the asset that has been moved as a result of transaction.
     */
    @Override
    protected void useTransactionData(String assetType, double price, double amount) {
        super.useTransactionData(assetType, price, amount);
        var stockData = (StockData) this.assetManager.getAssetData(assetType);
        var associatedCompany = stockData.getCompany();
        associatedCompany.recordTransactionData(price, amount);
    }

    /**
     * Returns the trading currency of the market as the currency of the asset.
     * @param assetType queried asset.
     * @return trading currency of the asset.
     */
    public String getAssetTradingCurrency(String assetType) {
        return this.tradingCurrency;
    }

    /**
     * Sets market currency to the given one. Raises an exception if non-existent currency is passed.
     * @param currency new currency of the market.
     */
    public void setMarketCurrency(String currency) {
        if (!this.assetManager.doesAssetExist(currency, AssetCategory.CURRENCY)
                && !currency.equals(Constants.DEFAULT_CURRENCY)) {
            throw new IllegalArgumentException("Non-existent currency passed as a market currency: " + currency);
        }
        this.tradingCurrency = currency;
    }

    public HashSet<StockMarketIndex> getStockMarketIndexes() {
        return this.stockMarketIndexes;
    }

    /**
     * Checks if the indexes have been extended by new companies, adds them to the pool of available assets and calls
     * them to send sell offers.
     */
    public void refreshAssets() {
        for (var idx : this.getStockMarketIndexes()) {
            for (var company : idx.getCompanies()) {
                if (!this.assetTypesOnMarket.contains(company.getAssociatedAsset())) {
                    this.assetTypesOnMarket.add(company.getAssociatedAsset());
                    company.sellAllStocks(this);
                }
            }
        }
    }
}
