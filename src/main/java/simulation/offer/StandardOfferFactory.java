package main.java.simulation.offer;

public class StandardOfferFactory implements OfferFactory {
    private static int id = 0;

    @Override
    public BuyOffer createBuyOffer(String assetType, BuyingEntity sender, double price, double size, String currency) {
        return new BuyOffer(id++, assetType, sender, price, size, currency);
    }

    @Override
    public SellOffer createSellOffer(String assetType, SellingEntity sender, double price, double size, String currency) {
        return new SellOffer(id++, assetType, sender, price, size, currency);
    }
}
