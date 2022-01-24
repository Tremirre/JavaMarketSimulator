package simulation.offer;

/**
 * Standard factory creating offers and tracking the latest free ids.
 */
public class StandardOfferFactory implements OfferFactory {
    private static int id = 0;

    /**
     *
     * {@inheritDoc}
     * Sets the new offer id to the latest unused id and increments the latest unused id.
     */
    @Override
    public BuyOffer createBuyOffer(String assetType, BuyingEntity sender, double price, double size, String currency) {
        return new BuyOffer(id++, assetType, sender, price, size, currency);
    }

    /**
     *
     * {@inheritDoc}
     * Sets the new offer id to the latest unused id and increments the latest unused id.
     */
    @Override
    public SellOffer createSellOffer(String assetType, SellingEntity sender, double price, double size, String currency) {
        return new SellOffer(id++, assetType, sender, price, size, currency);
    }
}
