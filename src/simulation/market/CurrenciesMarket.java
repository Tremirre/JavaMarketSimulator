package simulation.market;

import simulation.asset.RandomNonDiscreteAssetFactory;
import simulation.util.RandomService;

import java.util.HashSet;

public class CurrenciesMarket extends Market {
    private HashSet<String> currencies;
    private InitialVoidSeller ivs;

    public CurrenciesMarket(String name, double buyFee, double sellFee) {
        super(name + " Currency", buyFee, sellFee);
    }

    @Override
    public void initializeMarket() {
        this.currencies = new HashSet<>();
        this.ivs = new InitialVoidSeller();
        var initialNumberOfCurrencies = RandomService.getInstance().yieldRandomNumber(5) + 1;
        for (int i = 0; i < initialNumberOfCurrencies; i++) {
            var newCurrency = new RandomNonDiscreteAssetFactory().createCurrencyAsset();
            this.addNewCurrency(newCurrency);
        }
    }

    @Override
    public synchronized HashSet<String> getAvailableAssetTypes() {
        return this.currencies;
    }

    public void addNewCurrency(String currency) {
        this.currencies.add(currency);
        this.ivs.sendInitialOffer(this, currency);
    }
}
