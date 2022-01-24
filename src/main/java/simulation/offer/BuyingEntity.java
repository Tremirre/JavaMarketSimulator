package simulation.offer;

/**
 * Interface defining an entity that may be the sender of BuyOffers.
 */
public interface BuyingEntity extends OfferingEntity {
    /**
     * Function that processes transaction from the side of the buyer.
     * @param assetType the type of the bought asset.
     * @param overPay overpaid amount of money in DEFAULT STANDARD CURRENCY.
     * @param amount of asset that has been bought.
     */
    default void processBuyOffer(String assetType, double overPay, double amount) {
    }

    /**
     * Function that processes buy offer withdrawal.
     * @param price amount of money to be returned in given currency per 1 unit of asset.
     * @param amount amount of asset that has been offered for buying.
     * @param currency currency in which the offer was made.
     */
    default void processBuyWithdrawal(double price, double amount, String currency) {
    }

    /**
     * Function that given offer parameters returns a new price of that offer.
     * @param price amount of money originally offered for 1 unit of asset in given currency.
     * @param amount amount of asset that has been offered for buying.
     * @param assetType type of the asset to be bought.
     * @param currency currency of the offer.
     * @return modified price of the offer in offer currency per 1 unit of asset.
     */
    default double processBuyOfferAlteration(double price, double amount, String assetType, String currency) {
        return price;
    }
}
