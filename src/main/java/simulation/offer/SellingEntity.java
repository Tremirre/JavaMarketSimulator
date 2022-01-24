package simulation.offer;

/**
 * Interface defining an entity that can be an author of sell offers.
 */
public interface SellingEntity extends OfferingEntity {
    /**
     * Function that processes transaction from the side of the seller.
     * @param assetType asset to be sold.
     * @param price price per 1 unit of asset in DEFAULT STANDARD CURRENCY.
     * @param amount amount of asset to be sold.
     */
    default void processSellOffer(String assetType, double price, double amount) {
    }

    /**
     * Function that processes sell offer withdrawal.
     * @param assetType asset of the offer to be withdrawn.
     * @param amount amount of the asset of the withdrawn offer.
     */
    default void processSellWithdrawal(String assetType, double amount) {
    }

    /**
     * Function that given offer parameters returns a new price of that offer.
     * @param price price of the offer in given currency per 1 unit of asset.
     * @param assetType asset of the offer.
     * @param currency currency of the offer.
     * @return new price of the offer per 1 unit of asset in offer currency.
     */
    default double processSellOfferAlteration(double price, String assetType, String currency) {
        return price;
    }
}
