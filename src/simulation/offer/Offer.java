package simulation.offer;

import simulation.holders.AssetHolder;

public abstract class Offer {
    private int id;
    private String assetType;
    private AssetHolder sender;
    private double price;
    private double size;
    private int daysSinceGiven = 0;

    Offer(int id, String assetType, AssetHolder sender, double price, double size) {
        this.id = id;
        this.assetType = assetType;
        this.sender = sender;
        this.price = price;
        this.size = size;
    }

    public void updatePrice(){

    }
}
