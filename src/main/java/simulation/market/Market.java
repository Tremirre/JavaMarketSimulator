package simulation.market;

import simulation.core.SimulationConfig;
import simulation.asset.AssetManager;
import simulation.address.Address;
import simulation.offer.*;
import simulation.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a market.
 */
abstract public class Market {
    /**
     * Name and identifier of the market.
     */
    private String name;
    /**
     * Market address.
     */
    private Address address;
    private double buyFee;
    private double sellFee;
    private ArrayList<BuyOffer> buyOffers;
    private ArrayList<SellOffer> sellOffers;
    /**
     * Reference to the asset manager.
     */
    protected final AssetManager assetManager;
    /**
     * Set of assets traded on the market.
     */
    protected HashSet<String> assetTypesOnMarket;
    private int transactionsCount = 0;
    private int updatedCount = 0;
    private int removedCount = 0;

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public int getRemovedCount() {
        return removedCount;
    }

    /**
     * Creates new market with given parameters.
     * @param assetManager Reference to asset manager.
     * @param name Prefix of the name of the market.
     * @param buyFee Buy fee on the market.
     * @param sellFee Sell fee on the market.
     * @param address Address of the market.
     */
    Market(AssetManager assetManager, String name, double buyFee, double sellFee, Address address) {
        this.assetManager = assetManager;
        this.address = address;
        this.name = name + " Market of " + this.address.getCity();
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.buyOffers = new ArrayList<>();
        this.sellOffers = new ArrayList<>();
        this.assetTypesOnMarket = new HashSet<>();
    }

    /**
     * Adds new asset to assets available for trading on that market.
     * @param assetType asset to be added.
     */
    abstract public void addNewAsset(String assetType);

    /**
     * Creates buy offer and adds it to the pool of buy offers on the market. Currency is determined by the market.
     * @param assetType asset offered for buying.
     * @param sender author of the offer.
     * @param price Maximal price for 1 unit of asset in offer currency buyer is willing to pay.
     * @param size amount of asset offered for buying.
     */
    public synchronized void addBuyOffer(String assetType, BuyingEntity sender, double price, double size) {
        var offerCurrency = this.getAssetTradingCurrency(assetType);
        BuyOffer offer = new StandardOfferFactory().createBuyOffer(assetType, sender, price, size, offerCurrency);
        this.buyOffers.add(offer);
    }

    /**
     * Creates sell offer and adds it to the pool of sell offers on the market. Currency is determined by the market.
     * @param assetType asset offered for buying.
     * @param sender author of the offer.
     * @param price price of the offer for 1 unit of asset in offer currency.
     * @param size size of the offer
     */
    public synchronized void addSellOffer(String assetType, SellingEntity sender, double price, double size) {
        var offerCurrency = this.getAssetTradingCurrency(assetType);
        SellOffer offer = new StandardOfferFactory().createSellOffer(assetType, sender, price, size, offerCurrency);
        this.sellOffers.add(offer);
    }

    /**
     * Processes transaction between buy offer and the sell offer.
     * Price is determined by the sell offer (no higher than the one in buy offer).
     * This price is also recorded to the price history of the asset.
     * Amount of money is converted to DEFAULT STANDARD CURRENCY.
     * In case sizes of the offers differ, the smaller one is picked as the common one.
     * If buyer's offer is for a larger amount, the amount of money he overpaid is returned to him.
     * @param buyOffer valid buy offer to be processed.
     * @param sellOffer valid sell offer to be processed.
     * @return boolean denoting whether the sell offer has been fully exhausted and can be removed.
     */
    protected boolean processTransaction(BuyOffer buyOffer, SellOffer sellOffer) {
        this.transactionsCount++;
        var buyer = buyOffer.getSender();
        var seller = sellOffer.getSender();
        var commonPrice = sellOffer.getPrice();
        var assetType = sellOffer.getAssetType();
        var amount = Math.min(sellOffer.getSize(), buyOffer.getSize());
        var priceDiff = buyOffer.getPrice() * buyOffer.getSize() - commonPrice * amount;
        var latestOfferCurrencyRate = this.assetManager.findPrice(buyOffer.getOfferCurrency());
        var convertedCommonPrice = commonPrice * latestOfferCurrencyRate;
        buyer.processBuyOffer(assetType, priceDiff * latestOfferCurrencyRate, amount);
        seller.processSellOffer(assetType, convertedCommonPrice, amount);
        this.useTransactionData(assetType, convertedCommonPrice, amount);
        sellOffer.setSize(sellOffer.getSize() - amount);
        return (sellOffer.getSize() > 0);
    }

    /**
     * Uses data for recorded transaction, by default only records the price of the transaction.
     * @param assetType asset of the transaction.
     * @param price price of the transaction in DEFAULT STANDARD CURRENCY for 1 unit of the asset.
     * @param amount amount of the asset that has been moved as a result of transaction.
     */
    protected void useTransactionData(String assetType, double price, double amount) {
        this.assetManager.getAssetData(assetType).addLatestSellingPrice(price);
    }

    /**
     * Processes all compatible transactions from the pool of buy/sell offers until reaching the limit of
     * transactions per day per market. Removes all processed buy offers and exhausted sell offers.
     */
    public void processAllOffers() {
        this.transactionsCount = 0;
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

    /**
     * Utility function for removing offer with given id from the list of offers.
     * @param offers list of offers.
     * @param offerID identifier of an offer to be removed.
     */
    protected void removeFromOffersList(ArrayList<? extends Offer> offers, int offerID) {
        for (var offer : offers) {
            if (offer.getID() == offerID) {
                offers.remove(offer);
                return;
            }
        }
    }

    /**
     * Removes a buy offer from the list of buy offers.
     * @param offerID identifier of an offer to be removed.
     */
    public void removeBuyOffer(int offerID) {
        this.removeFromOffersList(this.buyOffers, offerID);
    }

    /**
     * Removes a sell offer from the list of sell offers.
     * @param offerID identifier of an offer to be removed.
     */
    public void removeSellOffer(int offerID) {
        this.removeFromOffersList(this.sellOffers, offerID);
    }

    /**
     * Updates all offers, by incrementing their day counter and updating their price.
     */
    public void updateOffers() {
        this.updatedCount = 0;
        var allOffers = new HashSet<Offer>(this.buyOffers);
        allOffers.addAll(this.sellOffers);
        for (var offer : allOffers) {
            this.updatedCount++;
            offer.updatePrice();
            offer.makeOlder();
        }
    }

    /**
     * Removes all outdated offers that can be removed (that is, that their sender can remove).
     */
    public void removeOutdatedOffers() {
        this.removedCount = 0;
        var offersForRemoval = new HashSet<Integer>();
        var allOffers = new HashSet<Offer>(this.buyOffers);
        allOffers.addAll(this.sellOffers);
        for (var offer : allOffers) {
            if (offer.getDaysSinceGiven() > SimulationConfig.getInstance().getMaxOfferAge()
                    && offer.getSender().canWithdraw(offer.getAssetType())) {
                this.removedCount++;
                offer.withdraw();
                offersForRemoval.add(offer.getID());
            }
        }
        this.buyOffers.removeIf(offer -> offersForRemoval.contains(offer.getID()));
        this.sellOffers.removeIf(offer -> offersForRemoval.contains(offer.getID()));
    }

    /**
     * Counts offers of the sender present on the market.
     * @param id of the sender.
     * @return number of offers of that sender on the market.
     */
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

    public HashSet<String> getAvailableAssetTypes() {
        return this.assetTypesOnMarket;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
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

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    /**
     * Returns trading currency of the asset. By default, returns the DEFAULT TRADING CURRENCY.
     * @param assetType queried asset.
     * @return asset trading currency.
     */
    public String getAssetTradingCurrency(String assetType) {
        return Constants.DEFAULT_CURRENCY;
    }

    public int getSellOfferCount() {
        return this.sellOffers.size();
    }

    public int getBuyOfferCount() {
        return this.buyOffers.size();
    }
}
