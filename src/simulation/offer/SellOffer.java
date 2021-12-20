package simulation.offer;

import simulation.holders.AssetHolder;

public class SellOffer extends Offer {
    SellOffer(int id, String assetType, AssetHolder sender, double price, double size) {
        super(id, assetType, sender, price, size);
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processSellOfferAlteration(this.price);
    }

    @Override
    public void withdraw() {
        this.sender.processSellWithdrawal(this.assetType, this.size);
    }
}
