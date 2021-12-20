package simulation.offer;

import simulation.holders.AssetHolder;

public class BuyOffer extends Offer {
    BuyOffer(int id, String assetType, AssetHolder sender, double price, double size) {
        super(id, assetType, sender, price, size);
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processBuyOrderAlteration(this.price, this.size);
    }

    @Override
    public void withdraw() {
        this.sender.processBuyWithdrawal(this.price, this.size);
    }
}
