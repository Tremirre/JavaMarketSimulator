package simulation.offer;

public interface IOfferFactory {
    BuyOffer createBuyOffer(String assetType, BuyingEntity sender, double price, double size);
    SellOffer createSellOffer(String assetType, SellingEntity sender, double price, double size);
}
