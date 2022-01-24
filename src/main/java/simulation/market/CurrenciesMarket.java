package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.address.Address;

/**
 * Class representing currency market.
 */
public class CurrenciesMarket extends Market {
    /**
     * Initial void seller tasked with sending initial offers for new assets.
     */
    private InitialVoidSeller ivs = new InitialVoidSeller();

    /**
     * Constructor matching parent constructor.
     * Appends the provided prefix name by the type of the market.
     * @param assetManager AssetManager reference.
     * @param name Prefix of the name of the market.
     * @param buyFee Buy fee on the market.
     * @param sellFee Sell fee on the market.
     * @param address Address of the market.
     */
    public CurrenciesMarket(AssetManager assetManager, String name, double buyFee, double sellFee, Address address) {
        super(assetManager, name + " Currency", buyFee, sellFee, address);
    }

    /**
     * Adds new currency to the pool of available currencies on the market. Throws exception if non-existing currency
     * identifier is passed. Calls initial void seller to send initial offer to the market.
     * @param currency asset to be added to the market.
     */
    public void addNewAsset(String currency) {
        if (!this.assetManager.doesAssetExist(currency, AssetCategory.CURRENCY))
            throw new IllegalArgumentException("Invalid asset type passed to a currency market: " + currency);
        this.assetTypesOnMarket.add(currency);
        this.ivs.sendInitialOffer(this, currency);
    }
}
