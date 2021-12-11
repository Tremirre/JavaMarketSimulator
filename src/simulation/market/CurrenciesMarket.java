package simulation.market;

import simulation.asset.AssetManager;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.io.IOException;
import java.util.HashSet;

public class CurrenciesMarket extends Market{
    private HashSet<String> currencies;

    public CurrenciesMarket(String name, double buyFee, double sellFee) throws IOException {
        super(name + " Currency", buyFee, sellFee);
        this.currencies = new HashSet<String>();
        initializeMarket();
    }

    @Override
    public void initializeMarket() {
        var manager = AssetManager.getInstance();
        var newCurrency = manager.createCurrencyAsset("US Dollar", 1.00, new String[]{"United States"});
        this.currencies.add(newCurrency.getUniqueIdentifyingName());
    }

    @Override
    void processOffer(BuyOffer buyOffer, SellOffer sellOffer) {

    }

    @Override
    public void processAllOffers() {

    }

    @Override
    public String[] getAvailableAssetTypes() {
        return this.currencies.toArray(new String[0]);
    }
}
