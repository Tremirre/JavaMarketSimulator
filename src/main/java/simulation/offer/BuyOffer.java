package main.java.simulation.offer;

public class BuyOffer extends Offer {
    protected BuyingEntity sender;

    BuyOffer(int id, String assetType, BuyingEntity sender, double price, double size, String currency) {
        super(id, assetType, price, size, currency);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processBuyOfferAlteration(this.price, this.size, this.assetType, this.getOfferCurrency());
    }

    @Override
    public void withdraw() {
        this.sender.processBuyWithdrawal(this.price, this.size, this.getOfferCurrency());
    }

    public BuyingEntity getSender() {return this.sender;}
}
