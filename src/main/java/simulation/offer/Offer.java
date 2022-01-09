package main.java.simulation.offer;

public abstract class Offer {
    protected int id;
    protected String assetType;
    protected OfferingEntity sender;
    protected double price;
    protected double size;
    protected String offerCurrency;
    protected int daysSinceGiven = 0;

    Offer(int id, String assetType, double price, double size, String currency) {
        this.id = id;
        this.assetType = assetType;
        this.price = price;
        this.size = size;
        this.offerCurrency = currency;
    }

    public String getOfferCurrency() {
        return offerCurrency;
    }

    public void setOfferCurrency(String offerCurrency) {
        this.offerCurrency = offerCurrency;
    }

    abstract public void updatePrice();

    abstract public void withdraw();

    public int getID() {
        return this.id;
    }

    public String getAssetType() {
        return this.assetType;
    }

    public double getPrice() {
        return this.price;
    }

    public double getSize() {
        return this.size;
    }

    public OfferingEntity getSender() {return this.sender;}

    public int getDaysSinceGiven() {
        return this.daysSinceGiven;
    }

    public void makeOlder() {
        this.daysSinceGiven++;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
