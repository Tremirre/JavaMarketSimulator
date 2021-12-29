package simulation.offer;

import simulation.holders.AssetHolder;

public class StandardOfferFactory implements IOfferFactory {
    private static int id = 0;

    @Override
    public Offer createOffer(String assetType, AssetHolder sender, double price, double size, boolean sellOffer) {
        if(sellOffer) {
            return new SellOffer(id++, assetType, sender, price, size);
        }
        return new BuyOffer(id++, assetType, sender, price, size);
    }
}
