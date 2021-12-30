package simulation.offer;

public interface BuyingEntity extends Entity {
    default void processBuyOffer(String assetType, double overPay, double amount) {
    }

    default void processBuyWithdrawal(double price, double amount) {
    }

    default double processBuyOfferAlteration(double price, double amount) {
        return price;
    }
}
