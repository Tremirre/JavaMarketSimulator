package simulation.offer;

import simulation.asset.AssetHolder;

public interface IOfferFactory {
    Offer createOffer(String assetType, AssetHolder sender, double price, double size, boolean sellOffer);
}
