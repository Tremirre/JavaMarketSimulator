package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.util.RandomDataGenerator;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AssetHolder extends Thread {
    HashMap<String, Double> storedAssets;
    HashMap<String, Double> assetsOnSale;
    private HashSet<Market> availableMarkets;
    protected double investmentBudget;
    protected double frozenFunds;
    protected boolean running = false;
    private int id;

    public AssetHolder(int id, double investmentBudget) {
        this.storedAssets = new HashMap<>();
        this.assetsOnSale = new HashMap<>();
        this.availableMarkets = new HashSet<>();
        this.investmentBudget = investmentBudget;
        this.frozenFunds = 0;
        this.id = id;
    }

    public void sendSellOrder(Market market) {
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

    public void sendBuyOrder(Market market) {
        var rand = RandomDataGenerator.getInstance();
        String chosenAsset = (String) rand.sampleElement(market.getAvailableAssetTypes().toArray());
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestSellingPrice() * 0.9;
        double amount = this.investmentBudget > price * 2 && rand.yieldRandomNumber(1.0) > 0.7 ? 1.0 : 2.0;
        market.addBuyOffer(chosenAsset, this, price, amount);
        this.investmentBudget -= price * amount;
        this.frozenFunds += price * amount;
    }

    public void processBuyOrder(String assetType, double price, double amount) {
        double newAmount = this.storedAssets.getOrDefault(assetType, 0.0) + amount;
        this.frozenFunds -= price * amount;
        this.storedAssets.put(assetType, newAmount);
    }

    public void processSellOrder(String assetType, double price, double amount) {
        if (!this.assetsOnSale.containsKey(assetType)) {
            // exception
            return;
        }
        double newAmount = this.assetsOnSale.get(assetType) - amount;
        this.investmentBudget += price * amount;
        if (newAmount > 0)
            this.assetsOnSale.replace(assetType, newAmount);
        else
            this.assetsOnSale.remove(assetType);
    }

    public void generateOrders() {
        var rand = RandomDataGenerator.getInstance();
        var market = (Market) rand.sampleElement(this.availableMarkets.toArray());
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendBuyOrder(market);
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendSellOrder(market);
    }

    public void giveAccessToMarkets(HashSet<Market> markets) {
        this.availableMarkets = markets;
    }

    public int getID() {
        return this.id;
    }

    public void stopRunning() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void start() {
        this.running = true;
        super.start();
    }

    abstract public void run();
}
