package simulation.market;

import simulation.offer.SellingEntity;
import simulation.util.RandomService;

/**
 * Class representing an entity tasked with sending initial offers to the market.
 */
public class InitialVoidSeller implements SellingEntity {
    public void sendInitialOffer(Market market, String assetType) {
        var assetCurrencyRate = market.getAssetManager().findPrice(market.getAssetTradingCurrency(assetType));
        var price = market.getAssetManager().getAssetData(assetType).getOpeningPrice() / assetCurrencyRate;
        var size = RandomService.getInstance().yieldRandomGaussianNumber(25, 100);
        market.addSellOffer(assetType, this, price, size);
    }

    /**
     * Returns special id reserved for IVS instances
     * @return initial void seller common id.
     */
    @Override
    public int getID() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * Set to be always false for ivs.
     */
    @Override
    public boolean canWithdraw(String assetType) {
        return false;
    }
}
