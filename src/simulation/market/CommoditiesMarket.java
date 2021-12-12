package simulation.market;

import java.io.IOException;
import java.util.HashSet;

public class CommoditiesMarket extends Market{
    private HashSet<String> commodities;

    public CommoditiesMarket(String name, double buyFee, double sellFee) throws IOException {
        super(name + " Commodity", buyFee, sellFee);
        this.commodities = new HashSet<>();
        this.initializeMarket();
    }

    @Override
    public void initializeMarket() {
    }

    @Override
    public HashSet<String> getAvailableAssetTypes() {
        return this.commodities;
    }
}
