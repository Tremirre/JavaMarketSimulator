package simulation.offer;

import simulation.holders.AssetHolder;

public abstract class Offer {
    protected int id;
    protected String assetType;
    protected AssetHolder sender;
    protected double price;
    protected double size;
    protected int daysSinceGiven = 0;

    Offer(int id, String assetType, AssetHolder sender, double price, double size) {
        this.id = id;
        this.assetType = assetType;
        this.sender = sender;
        this.price = price;
        this.size = size;
    }

    public void updatePrice(){

    }

    public int getID() {
        return id;
    }

    public String getAssetType() {
        return assetType;
    }

    public AssetHolder getSender() {
        return sender;
    }

    public double getPrice() {
        return price;
    }

    public double getSize() {
        return size;
    }

    public int getDaysSinceGiven() {
        return daysSinceGiven;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
