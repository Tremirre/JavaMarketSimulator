package simulation.offer;

/**
 * Interface class for all offer factories.
 */
public interface OfferFactory {
    /**
     * Given set of parameters returns a buyOffer
     * @param assetType asset to be bought.
     * @param sender buyer.
     * @param price maximal price for 1 unit of asset in given currency buyer is willing to pay.
     * @param size amount of asset to be bought.
     * @param currency offer currency.
     * @return new buyOffer.
     */
    BuyOffer createBuyOffer(String assetType, BuyingEntity sender, double price, double size, String currency);

    /**
     * Given set of parameters returns a sellOffer
     * @param assetType asset to be sold.
     * @param sender seller.
     * @param price price for 1 unit of asset in given currency.
     * @param size amount of asset to be sold.
     * @param currency currency of the offer.
     * @return new sellOffer.
     */
    SellOffer createSellOffer(String assetType, SellingEntity sender, double price, double size, String currency);
}
