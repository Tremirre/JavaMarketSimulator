package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.util.RandomService;

public class InitialOffersSender extends AssetHolder  {
    public InitialOffersSender() {
        super(-1, 0);
        this.freezeWithdrawal = true;
    }

    public void sendInitialOffer(Market market, String assetType) {
        var price = AssetManager.getInstance().getAssetData(assetType).getLatestAverageSellingPrice();
        var size = RandomService.getInstance().yieldRandomGaussianNumber(25, 100);
        market.addSellOffer(assetType, this, price, size);
    }

    @Override
    public void sendBuyOffer(Market market) {}

    @Override
    public void sendSellOffer(Market market) {}

    @Override
    public void processSellOffer(String assetType, double price, double amount) {}

    @Override
    public double processSellOfferAlteration(double price) {
        return price;
    }

    @Override
    public void print() {
        System.out.println("Initial Offers Sender for continuous assets");
    }
}
