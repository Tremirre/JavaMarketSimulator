package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.address.Address;

public class InformedMarketFactory extends MarketFactory{
    private String name;
    private Address address;
    private double buyFee;
    private double sellFee;

    public InformedMarketFactory(AssetManager assetManager, String name, Address address, double buyFee, double sellFee) {
        super(assetManager);
        this.name = name;
        this.address = address;
        this.buyFee = buyFee;
        this.sellFee = sellFee;
    }

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
