package simulation.market;

import simulation.asset.RandomNonDiscreteAssetFactory;
import simulation.holders.InitialOffersSender;
import simulation.util.RandomService;

import java.util.HashSet;

public class CommoditiesMarket extends Market{
    private HashSet<String> commodities;
    private InitialOffersSender ios;

    public CommoditiesMarket(String name, double buyFee, double sellFee) {
        super(name + " Commodity", buyFee, sellFee);
    }

    @Override
    public void initializeMarket() {
        this.commodities = new HashSet<>();
        this.ios = new InitialOffersSender();
        var inititalNumberOfCommodities = RandomService.getInstance().yieldRandomInteger(5) + 1;
        for (int i = 0; i< inititalNumberOfCommodities; i++) {
            var newCommodity = new RandomNonDiscreteAssetFactory().createCommodityAsset();
            this.addNewCommodity(newCommodity);
        }
    }

    @Override
    public synchronized HashSet<String> getAvailableAssetTypes() {
        return this.commodities;
    }

    public void addNewCommodity(String commodity) {
        this.commodities.add(commodity);
        this.ios.sendInitialOffer(this, commodity);
    }
}
