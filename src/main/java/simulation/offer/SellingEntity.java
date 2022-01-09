package main.java.simulation.offer;

public interface SellingEntity extends OfferingEntity {
    default void processSellOffer(String assetType, double price, double amount) {}
    default void processSellWithdrawal(String assetType, double amount) {}
    default double processSellOfferAlteration(double price, String assetType, String currency) {return price;}
}
