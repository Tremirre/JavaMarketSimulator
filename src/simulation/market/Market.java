package simulation.market;

import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.util.ArrayList;

abstract public class Market {
    private String name;
    private Address address;
    private double buyFee;
    private double sellFee;
    private ArrayList<BuyOffer> buyOffers;
    private ArrayList<SellOffer> sellOffers;

    abstract public void initializeMarket();

    abstract public void addBuyOffer(String assetType, AssetHolder sender, double price, double size);

    abstract public void addSellOffer(String assetType, AssetHolder sender, double price, double size);

    abstract void processOffer(BuyOffer buyOffer, SellOffer sellOffer);

    abstract public void processAllOffers();

    public void removeBuyOffer(int offerID) {

    }

    public void removeSellOffer(int offerID) {

    }

    public void removeOutdatedOffers() {

    }

    public int countSenderOffers(AssetHolder sender) {
        return 0;
    }

    abstract public ArrayList<String> getAvailableAssetTypes();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
