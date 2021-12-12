package simulation.market;

import simulation.asset.AssetManager;
import simulation.holders.Address;
import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;
import simulation.offer.StandardOfferFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

abstract public class Market {
    final static int MAX_DAYS = 10;
    private String name;
    private Address address;
    private double buyFee;
    private double sellFee;
    private ArrayList<BuyOffer> buyOffers;
    private ArrayList<SellOffer> sellOffers;

    Market(String name, double buyFee, double sellFee) throws IOException {
        this.address = Address.getRandomAddress();
        this.name = name + " Market of " + this.address.getCity();
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.buyOffers = new ArrayList<BuyOffer>();
        this.sellOffers = new ArrayList<SellOffer>();
    }

    abstract public void initializeMarket();

    public void addBuyOffer(String assetType, AssetHolder sender, double price, double size) {
        BuyOffer offer = (BuyOffer) new StandardOfferFactory().createOffer(assetType, sender, price, size, false);
        this.buyOffers.add(offer);
    }

    public void addSellOffer(String assetType, AssetHolder sender, double price, double size) {
        SellOffer offer = (SellOffer) new StandardOfferFactory().createOffer(assetType, sender, price, size, true);
        this.sellOffers.add(offer);
    }

    private void processOffer(BuyOffer buyOffer, SellOffer sellOffer) {
        var buyer = buyOffer.getSender();
        var seller = sellOffer.getSender();
        var commonPrice = sellOffer.getPrice();
        var assetType = sellOffer.getAssetType();
        var amount = sellOffer.getSize();
        buyer.processBuyOrder(assetType, commonPrice, amount);
        seller.processSellOrder(assetType, commonPrice, amount);
        AssetManager.getInstance().getAssetData(assetType).addLatestSellingPrice(commonPrice);
    }

    public void processAllOffers() {
        ArrayList<Integer> processedSellOrders = new ArrayList<>();
        for (SellOffer sellOffer : this.sellOffers) {
            for (BuyOffer buyOffer: this.buyOffers) {
                if (!sellOffer.getAssetType().equals(buyOffer.getAssetType()) ||
                        sellOffer.getPrice() > buyOffer.getPrice()) continue;
                this.processOffer(buyOffer, sellOffer);
                processedSellOrders.add(sellOffer.getID());
                this.removeBuyOffer(buyOffer.getID());
                break;
            }
            if (processedSellOrders.size() > 20)
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

    public void removeOutdatedOffers() {
        buyOffers.removeIf(offer -> offer.getDaysSinceGiven() > MAX_DAYS);
        sellOffers.removeIf(offer -> offer.getDaysSinceGiven() > MAX_DAYS);
    }

    public int countSenderOffers(AssetHolder sender) {
        int total = 0;
        for (var offer : this.buyOffers) {
            if (offer.getSender().hashCode() == sender.hashCode())
                total++;
        }
        for (var offer : this.sellOffers) {
            if (offer.getSender().hashCode() == sender.hashCode())
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
