package simulation.market;

import simulation.SimulationConfig;
import simulation.asset.AssetManager;
import simulation.holders.Address;
import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;
import simulation.offer.StandardOfferFactory;

import java.util.ArrayList;
import java.util.HashSet;

abstract public class Market {
    final static int MAX_DAYS = 20;
    private String name;
    private Address address;
    private double buyFee;
    private double sellFee;
    private ArrayList<BuyOffer> buyOffers;
    private ArrayList<SellOffer> sellOffers;

    Market(String name, double buyFee, double sellFee) {
        this.address = Address.getRandomAddress();
        this.name = name + " Market of " + this.address.getCity();
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.buyOffers = new ArrayList<>();
        this.sellOffers = new ArrayList<>();
        this.initializeMarket();
    }

    abstract public void initializeMarket();

    public synchronized void addBuyOffer(String assetType, AssetHolder sender, double price, double size) {
        BuyOffer offer = (BuyOffer) new StandardOfferFactory().createOffer(assetType, sender, price, size, false);
        this.buyOffers.add(offer);
    }

    public synchronized void addSellOffer(String assetType, AssetHolder sender, double price, double size) {
        SellOffer offer = (SellOffer) new StandardOfferFactory().createOffer(assetType, sender, price, size, true);
        this.sellOffers.add(offer);
    }

    private boolean processOffer(BuyOffer buyOffer, SellOffer sellOffer) {
        var buyer = buyOffer.getSender();
        var seller = sellOffer.getSender();
        var commonPrice = sellOffer.getPrice();
        var assetType = sellOffer.getAssetType();
        var amount = Math.min(sellOffer.getSize(), buyOffer.getSize());
        buyer.processBuyOffer(assetType, commonPrice, amount);
        seller.processSellOffer(assetType, commonPrice, amount);
        AssetManager.getInstance().getAssetData(assetType).addLatestSellingPrice(commonPrice);
        sellOffer.setSize(sellOffer.getSize() - amount);
        return (sellOffer.getSize() > 0);
    }

    public synchronized void processAllOffers() {
        ArrayList<Integer> processedSellOrders = new ArrayList<>();
        for (SellOffer sellOffer : this.sellOffers) {
            for (BuyOffer buyOffer : this.buyOffers) {
                if (!sellOffer.getAssetType().equals(buyOffer.getAssetType()) ||
                        sellOffer.getPrice() > buyOffer.getPrice()) continue;
                if (!this.processOffer(buyOffer, sellOffer))
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

    public void removeBuyOffer(int offerID) {
        buyOffers.removeIf(offer -> offer.getID() == offerID);
    }

    public void removeSellOffer(int offerID) {
        sellOffers.removeIf(offer -> offer.getID() == offerID);
    }

    public synchronized void updateOffers() {
        for (var offer : this.sellOffers) {
            offer.updatePrice();
            offer.makeOlder();
        }
        for (var offer : this.buyOffers) {
            offer.updatePrice();
            offer.makeOlder();
        }
    }

    public synchronized void removeOutdatedOffers() {
        var offersForRemoval = new HashSet<Integer>();
        for (var offer : this.buyOffers) {
            if (offer.getDaysSinceGiven() > MAX_DAYS) {
                offer.withdraw();
                offersForRemoval.add(offer.getID());
            }
        }
        for (var offer : this.sellOffers) {
            if (offer.getDaysSinceGiven() > MAX_DAYS && offer.getSender().canWithdraw()) {
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

    abstract public HashSet<String> getAvailableAssetTypes();

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
}
