package simulation.market;

import simulation.asset.AssetManager;
import simulation.asset.StockData;
import simulation.holders.CompaniesManager;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;
import simulation.util.RandomService;

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
