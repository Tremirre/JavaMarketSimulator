package simulation.market;

import simulation.asset.AssetManager;
import simulation.offer.SellingEntity;
import simulation.util.RandomService;

public class InitialVoidSeller implements SellingEntity {
    public void sendInitialOffer(Market market, String assetType) {
        var assetManager = AssetManager.getInstance();
        var assetCurrencyRate = assetManager.findPrice(market.getAssetTradingCurrency(assetType));
        var price = assetManager.getAssetData(assetType).getOpeningPrice()*assetCurrencyRate;
        var size = RandomService.getInstance().yieldRandomGaussianNumber(25, 100);
        market.addSellOffer(assetType, this, price, size);
    }

    @Override
    public int getID() {
        return -1;
    }

    @Override
    public boolean canWithdraw() {
        return false;
    }
}
