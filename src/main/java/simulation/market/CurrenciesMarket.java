package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.address.Address;

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
