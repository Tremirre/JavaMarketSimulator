package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.address.Address;

/**
 * Class that creates new markets with provided data.
 */
public class InformedMarketFactory extends MarketFactory{
    /**
     * Name of the market to be created.
     */
    private String name;
    /**
     * Address of the market to be created.
     */
    private Address address;
    /**
     * Buy fee of the market to be created.
     */
    private double buyFee;
    /**
     * Sell fee of the market to be created.
     */
    private double sellFee;

    /**
     * Fills the factory with data.
     * @param assetManager reference to the asset manager.
     * @param name name of the market to be created.
     * @param address address of the market to be created.
     * @param buyFee buy fee of the market to be created.
     * @param sellFee sell fee of the market to be created.
     */
    public InformedMarketFactory(AssetManager assetManager, String name, Address address, double buyFee, double sellFee) {
        super(assetManager);
        this.name = name;
        this.address = address;
        this.buyFee = buyFee;
        this.sellFee = sellFee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Market createMarket(AssetCategory category) {
        switch(category) {
            case CURRENCY -> {
                return new CurrenciesMarket(assetManager, this.name, this.buyFee, this.sellFee, this.address);
            }
            case COMMODITY -> {
                return new CommoditiesMarket(assetManager, this.name, this.buyFee, this.sellFee, this.address);
            }
            case STOCK -> {
                return new StockMarket(assetManager, this.name, this.buyFee, this.sellFee, null, this.address);
            }
            default -> throw new IllegalStateException();
        }
    }
}
