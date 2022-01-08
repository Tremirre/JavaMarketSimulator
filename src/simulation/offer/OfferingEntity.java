package simulation.offer;

public interface OfferingEntity {
    int getID();
    boolean canWithdraw(String assetType);
}
