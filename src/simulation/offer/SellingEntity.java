package simulation.offer;

public interface SellingEntity extends Entity {
    default void processSellOffer(String assetType, double price, double amount) {}
    default void processSellWithdrawal(String assetType, double amount) {}
    default double processSellOfferAlteration(double price) {return price;}
}
