package simulation.offer;

public class SellOffer extends Offer {
    protected SellingEntity sender;

    SellOffer(int id, String assetType, SellingEntity sender, double price, double size) {
        super(id, assetType, price, size);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processSellOfferAlteration(this.price);
    }

    @Override
    public void withdraw() {
        this.sender.processSellWithdrawal(this.assetType, this.size);
    }
    public SellingEntity getSender() {return this.sender;}
}
