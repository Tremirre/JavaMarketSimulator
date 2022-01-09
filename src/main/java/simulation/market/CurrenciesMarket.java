package main.java.simulation.market;

import main.java.simulation.asset.AssetCategory;
import main.java.simulation.asset.AssetManager;
import main.java.simulation.holders.Address;

public class CurrenciesMarket extends Market {
    private InitialVoidSeller ivs = new InitialVoidSeller();

    public CurrenciesMarket(AssetManager assetManager, String name, double buyFee, double sellFee, Address address) {
        super(assetManager, name + " Currency", buyFee, sellFee, address);
    }

    public void addNewAsset(String currency) {
        if (!this.assetManager.doesAssetExist(currency, AssetCategory.CURRENCY))
            throw new IllegalArgumentException("Invalid asset type passed to a currency market: " + currency);
        this.assetTypesOnMarket.add(currency);
        this.ivs.sendInitialOffer(this, currency);
    }
}
