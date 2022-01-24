package simulation.offer;

/**
 * Represents common functions that both buying and selling entities have.
 */
public interface OfferingEntity {
    int getID();

    /**
     * Denotes whether the author of an offer can withdraw an offer of a given asset type.
     * @param assetType asset of the offer.
     * @return boolean flag saying whether the entity may withdraw the offer pertaining the given asset.
     */
    boolean canWithdraw(String assetType);
}
