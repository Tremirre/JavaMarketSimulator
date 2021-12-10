package simulation.market;

import simulation.holders.Address;
import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.ArrayList;

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
        this.name = name + " market of " + this.address.getCity();
        this.buyFee = buyFee;
        this.sellFee = sellFee;
        this.buyOffers = new ArrayList<BuyOffer>();
        this.sellOffers = new ArrayList<SellOffer>();
    }

    abstract public void initializeMarket();

    abstract public void addBuyOffer(String assetType, AssetHolder sender, double price, double size);

    abstract public void addSellOffer(String assetType, AssetHolder sender, double price, double size);

    abstract void processOffer(BuyOffer buyOffer, SellOffer sellOffer);

    abstract public void processAllOffers();

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

    abstract public ArrayList<String> getAvailableAssetTypes();

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
