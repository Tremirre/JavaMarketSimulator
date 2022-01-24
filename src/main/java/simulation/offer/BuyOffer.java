package simulation.offer;

/**
 * Class representing a buy offer.
 */
public class BuyOffer extends Offer {
    /**
     * Entity that sent the offer.
     */
    protected BuyingEntity sender;

    /**
     * Creates a new buy offer given the parameters.
     * @param id unique identifier of the offer.
     * @param assetType asset offered for buying.
     * @param sender author of the offer.
     * @param price Maximal price for 1 unit of the asset in offer currency sender is willing to accept.
     * @param size amount of asset offered for buying.
     * @param currency currency of the offer.
     */
    BuyOffer(int id, String assetType, BuyingEntity sender, double price, double size, String currency) {
        super(id, assetType, price, size, currency);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        this.price = this.sender.processBuyOfferAlteration(this.price, this.size, this.assetType, this.getOfferCurrency());
    }

    @Override
    public void withdraw() {
        this.sender.processBuyWithdrawal(this.price, this.size, this.getOfferCurrency());
    }

    public BuyingEntity getSender() {
        return this.sender;
    }
}
