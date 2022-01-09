package main.java.simulation.offer;

public class SellOffer extends Offer {
    protected SellingEntity sender;

    SellOffer(int id, String assetType, SellingEntity sender, double price, double size, String currency) {
        super(id, assetType, price, size, currency);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processSellOfferAlteration(this.price, this.assetType, this.getOfferCurrency());
    }

    @Override
    public void withdraw() {
        this.sender.processSellWithdrawal(this.assetType, this.size);
    }

    public SellingEntity getSender() {return this.sender;}
}
