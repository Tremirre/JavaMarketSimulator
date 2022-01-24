package simulation.offer;

/**
 * Class representing a sell offer.
 */
public class SellOffer extends Offer {
    /**
     * Entity that sent the offer.
     */
    protected SellingEntity sender;

    /**
     * Creates new sell offer with given parameters.
     * @param id unique identifier.
     * @param assetType asset to be sold.
     * @param sender author of the offer.
     * @param price price per 1 unit of an asset in offer currency.
     * @param size amount of asset to be sold.
     * @param currency currency of the offer.
     */
    SellOffer(int id, String assetType, SellingEntity sender, double price, double size, String currency) {
        super(id, assetType, price, size, currency);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processSellOfferAlteration(this.price, this.assetType, this.getOfferCurrency());
    }

    @Override
    public void withdraw() {
        this.sender.processSellWithdrawal(this.assetType, this.size);
    }

    public SellingEntity getSender() {
        return this.sender;
    }
}
