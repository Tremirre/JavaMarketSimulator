package simulation.offer;

/**
 * Class representing an offer.
 */
public abstract class Offer {
    /**
     * Unique offer identifier.
     */
    protected int id;
    /**
     * Asset that the offer concerns.
     */
    protected String assetType;
    /**
     * Author of the offer.
     */
    protected OfferingEntity sender;
    /**
     * Price of the offer per 1 unit of asset in offer currency.
     */
    protected double price;
    /**
     * Amount of asset that the offer concerns.
     */
    protected double size;
    /**
     * Currency of the offer.
     */
    protected String offerCurrency;
    /**
     * Number of days since the creation of the offer.
     */
    protected int daysSinceGiven = 0;

    /**
     * Creates an offer with given parameters.
     * @param id unique identifier.
     * @param assetType asset that the offer concerns.
     * @param price price of the offer per 1 unit of asset in offer currency.
     * @param size amount of asset that the offer concerns.
     * @param currency currency of the offer.
     */
    Offer(int id, String assetType, double price, double size, String currency) {
        this.id = id;
        this.assetType = assetType;
        this.price = price;
        this.size = size;
        this.offerCurrency = currency;
    }

    public String getOfferCurrency() {
        return offerCurrency;
    }

    /**
     * Updates the price according to the authors buy offer alteration policy.
     */
    abstract public void updatePrice();

    /**
     * Informs the author about the withdrawal and processes it according to his policy.
     */
    abstract public void withdraw();

    public int getID() {
        return this.id;
    }

    public String getAssetType() {
        return this.assetType;
    }

    public double getPrice() {
        return this.price;
    }

    public double getSize() {
        return this.size;
    }

    public OfferingEntity getSender() {
        return this.sender;
    }

    public int getDaysSinceGiven() {
        return this.daysSinceGiven;
    }

    /**
     * Makes the offer older by 1 day.
     */
    public void makeOlder() {
        this.daysSinceGiven++;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
