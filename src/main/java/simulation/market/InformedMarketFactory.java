package simulation.market;

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
    public Market createMarket(MarketType type) {
        return null;
    }
}
