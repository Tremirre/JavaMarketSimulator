package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.asset.StockData;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;
import simulation.util.Constants;

import java.util.HashSet;

public class StockMarket extends Market{
    private String tradingCurrency;
    private HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();

    public StockMarket(String name, double buyFee, double sellFee, String currency) {
        super(name + " Stock", buyFee, sellFee);
        this.tradingCurrency = currency;
    }

    public void addStockMarketIndex(StockMarketIndex idx) {
        this.stockMarketIndexes.add(idx);
        for (var company : idx.getCompanies()) {
            this.addNewAsset(company.getAssociatedAsset());
            company.sendInitialOffer(this);
        }
    }

    public void addNewAsset(String stock) {
        if (!AssetManager.getInstance().doesAssetExist(stock, AssetCategory.STOCK))
            throw new IllegalArgumentException("Invalid asset type passed to a stock market: " + stock);
        this.assetTypesOnMarket.add(stock);
    }

    @Override
    protected boolean processTransaction(BuyOffer buyOffer, SellOffer sellOffer) {
        var buyer = buyOffer.getSender();
        var seller = sellOffer.getSender();
        var commonPrice = sellOffer.getPrice();
        var assetType = sellOffer.getAssetType();
        var amount = Math.min(sellOffer.getSize(), buyOffer.getSize());
        var priceDiff = buyOffer.getPrice() * buyOffer.getSize() - commonPrice * amount;
        buyer.processBuyOffer(assetType, priceDiff, amount);
        seller.processSellOffer(assetType, commonPrice, amount);
        var stockData = (StockData) AssetManager.getInstance().getAssetData(assetType);
        stockData.addLatestSellingPrice(commonPrice);
        var associatedCompany = stockData.getCompany();
        associatedCompany.recordTransactionData(commonPrice, amount);
        sellOffer.setSize(sellOffer.getSize() - amount);
        return (sellOffer.getSize() > 0);
    }

    public String getAssetTradingCurrency(String assetType) {
        return Constants.DEFAULT_CURRENCY; //return this.tradingCurrency
    }
}
