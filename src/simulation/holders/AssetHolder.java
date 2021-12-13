package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.util.RandomDataGenerator;

import java.io.IOException;
import java.util.HashMap;

public abstract class AssetHolder extends Thread {
    HashMap<String, Double> storedAssets;
    HashMap<String, Double> assetsOnSale;

    protected double investmentBudget;
    protected double frozenFunds;
    private int id;

    public AssetHolder(int id, double investmentBudget) {
        this.storedAssets = new HashMap<>();
        this.assetsOnSale = new HashMap<>();
        this.investmentBudget = investmentBudget;
        this.frozenFunds = 0;
        this.id = id;
    }

    public void sendSellOrder(Market market) throws IOException {
        if (this.storedAssets.isEmpty())
            return;
        var rand = RandomDataGenerator.getInstance();
        String chosenAsset = (String) rand.sampleElement(this.storedAssets.keySet().toArray());
        double availableAmount = this.storedAssets.get(chosenAsset);
        double amount = availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestSellingPrice() * 1.1;
        market.addSellOffer(chosenAsset, this, price, amount);
        double left = this.storedAssets.get(chosenAsset) - amount;
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
    }

    public void sendBuyOrder(Market market) throws IOException {
        var rand = RandomDataGenerator.getInstance();
        String chosenAsset = (String) rand.sampleElement(market.getAvailableAssetTypes().toArray());
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestSellingPrice() * 0.9;
        double amount = this.investmentBudget > price * 2 && rand.yieldRandomNumber(1.0) > 0.7 ? 1.0 : 2.0;
        market.addBuyOffer(chosenAsset, this, price, amount);
        this.investmentBudget -= price * amount;
        this.frozenFunds += price * amount;
    }

    public void processBuyOrder(String assetType, double price, double amount) {
        if (!this.storedAssets.containsKey(assetType)) {
            return;
            //raise exception
        }
        double newAmount = this.assetsOnSale.get(assetType) - amount;
        if (newAmount > 0)
            this.assetsOnSale.replace(assetType, newAmount);
        else
            this.assetsOnSale.remove(assetType);
        this.frozenFunds -= price * amount;
    }

    public void processSellOrder(String assetType, double price, double amount) {
        this.investmentBudget += price * amount;
        this.storedAssets.put(assetType, this.storedAssets.getOrDefault(assetType, 0.0) + amount);
    }

    public void generateOrders(Market market) throws IOException {
        var rand = RandomDataGenerator.getInstance();
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendBuyOrder(market);
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendSellOrder(market);
    }

    public int getID() {
        return this.id;
    }

    abstract public void run();
}
