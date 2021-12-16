package simulation.market;

import java.util.HashSet;

public class CommoditiesMarket extends Market{
    private HashSet<String> commodities;

    public CommoditiesMarket(String name, double buyFee, double sellFee) {
        super(name + " Commodity", buyFee, sellFee);
        this.commodities = new HashSet<>();
        this.initializeMarket();
    }

    @Override
    public void initializeMarket() {
    }

    @Override
    public synchronized HashSet<String> getAvailableAssetTypes() {
        return this.commodities;
    }
}
