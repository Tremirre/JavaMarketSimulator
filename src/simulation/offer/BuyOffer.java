package simulation.offer;

public class BuyOffer extends Offer {
    protected BuyingEntity sender;

    BuyOffer(int id, String assetType, BuyingEntity sender, double price, double size) {
        super(id, assetType, price, size);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processBuyOfferAlteration(this.price, this.size, this.assetType);
    }

    @Override
    public void withdraw() {
        this.sender.processBuyWithdrawal(this.price, this.size);
    }

    public BuyingEntity getSender() {return this.sender;}
}
