package main.java.simulation.offer;

public interface BuyingEntity extends OfferingEntity {
    default void processBuyOffer(String assetType, double overPay, double amount) {
    }

    default void processBuyWithdrawal(double price, double amount, String currency) {
    }

    default double processBuyOfferAlteration(double price, double amount, String assetType, String currency) {
        return price;
    }
}
