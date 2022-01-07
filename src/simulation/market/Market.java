package simulation.market;

import simulation.SimulationConfig;
import simulation.asset.AssetManager;
import simulation.holders.Address;
import simulation.offer.*;
import simulation.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;

abstract public class Market {
    private String name;
    private Address address;
    private double buyFee;
    private double sellFee;
    private ArrayList<BuyOffer> buyOffers;
    private ArrayList<SellOffer> sellOffers;
    protected HashSet<String> assetTypesOnMarket;

    Market(String name, double buyFee, double sellFee, Address address) {
        this.address = address;
        this.name = name + " Market of " + this.address.getCity();
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.buyOffers = new ArrayList<>();
        this.sellOffers = new ArrayList<>();
        this.assetTypesOnMarket = new HashSet<>();
    }

    abstract public void addNewAsset(String assetType);

    public synchronized void addBuyOffer(String assetType, BuyingEntity sender, double price, double size, String offerCurrency) {
        BuyOffer offer = new StandardOfferFactory().createBuyOffer(assetType, sender, price, size, offerCurrency);
        this.buyOffers.add(offer);
    }

    public synchronized void addSellOffer(String assetType, SellingEntity sender, double price, double size) {
        var offerCurrency = this.getAssetTradingCurrency(assetType);
        SellOffer offer = new StandardOfferFactory().createSellOffer(assetType, sender, price, size, offerCurrency);
        this.sellOffers.add(offer);
    }

    protected boolean processTransaction(BuyOffer buyOffer, SellOffer sellOffer) {
        var buyer = buyOffer.getSender();
        var seller = sellOffer.getSender();
        var commonPrice = sellOffer.getPrice();
        var assetType = sellOffer.getAssetType();
        var amount = Math.min(sellOffer.getSize(), buyOffer.getSize());
        var priceDiff = buyOffer.getPrice() * buyOffer.getSize() - commonPrice * amount;
        var assetManager = AssetManager.getInstance();
        var latestOfferCurrencyRate = assetManager.findPrice(buyOffer.getOfferCurrency());
        var convertedCommonPrice = commonPrice/latestOfferCurrencyRate;
        buyer.processBuyOffer(assetType, priceDiff/latestOfferCurrencyRate, amount);
        seller.processSellOffer(assetType, convertedCommonPrice, amount);
        this.useTransactionData(assetType, convertedCommonPrice, amount);
        sellOffer.setSize(sellOffer.getSize() - amount);
        return (sellOffer.getSize() > 0);
    }

    protected void useTransactionData(String assetType, double price, double amount) {
        AssetManager.getInstance().getAssetData(assetType).addLatestSellingPrice(price);
    }

    public void processAllOffers() {
        ArrayList<Integer> processedSellOrders = new ArrayList<>();
        for (var sellOffer : this.sellOffers) {
            for (var buyOffer : this.buyOffers) {
                if (!sellOffer.getAssetType().equals(buyOffer.getAssetType()) ||
                        sellOffer.getPrice() > buyOffer.getPrice()) continue;
                if (!this.processTransaction(buyOffer, sellOffer))
                    processedSellOrders.add(sellOffer.getID());
                this.removeBuyOffer(buyOffer.getID());
                break;
            }
            if (processedSellOrders.size() > SimulationConfig.getInstance().getMaxTransactionsPerDayPerMarket())
                break;
        }
        for (var id : processedSellOrders)
            this.removeSellOffer(id);
    }

    protected void removeFromOffersList(ArrayList<? extends Offer> offers, int offerID) {
        for (var offer : offers) {
            if (offer.getID() == offerID) {
                offers.remove(offer);
                return;
            }
        }
    }

    public void removeBuyOffer(int offerID) {
        this.removeFromOffersList(this.buyOffers, offerID);
    }

    public void removeSellOffer(int offerID) {
        this.removeFromOffersList(this.sellOffers, offerID);
    }

    public void updateOffers() {
        var allOffers = new HashSet<Offer>(this.buyOffers);
        allOffers.addAll(this.sellOffers);
        for (var offer : allOffers) {
            offer.updatePrice();
            offer.makeOlder();
        }
    }

    public void removeOutdatedOffers() {
        var offersForRemoval = new HashSet<Integer>();
        var allOffers = new HashSet<Offer>(this.buyOffers);
        allOffers.addAll(this.sellOffers);
        for (var offer : allOffers) {
            if (offer.getDaysSinceGiven() > SimulationConfig.getInstance().getMaxOfferAge()
                    && offer.getSender().canWithdraw()) {
                offer.withdraw();
                offersForRemoval.add(offer.getID());
            }
        }
        this.buyOffers.removeIf(offer -> offersForRemoval.contains(offer.getID()));
        this.sellOffers.removeIf(offer -> offersForRemoval.contains(offer.getID()));
    }
    
    public synchronized int countSenderOffers(int id) {
        int total = 0;
        for (var offer : this.buyOffers) {
            if (offer.getSender().getID() == id)
                total++;
        }
        for (var offer : this.sellOffers) {
            if (offer.getSender().getID() == id)
                total++;
        }
        return total;
    }

    public HashSet<String> getAvailableAssetTypes() {return this.assetTypesOnMarket;}

    public Address getAddress() {
        return this.address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(double buyFee) {
        this.buyFee = buyFee;
    }

    public double getSellFee() {
        return sellFee;
    }

    public void setSellFee(double sellFee) {
        this.sellFee = sellFee;
    }

    public String getAssetTradingCurrency(String assetType) {
        return Constants.DEFAULT_CURRENCY;
    }
}
