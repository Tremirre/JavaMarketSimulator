package simulation.holders;

import simulation.market.Market;

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

    public void sendSellOrder(Market market) {
        if (this.storedAssets.isEmpty()) {
            return;
        }
    }

    public void sendBuyOrder(Market market) {

    }

    public void processBuyOrder(String assetType, double price, double amount) {

    }

    public void processSellOrder(String assetType, double price, double amount) {

    }

    public void generateOrders(Market market) {

    }

    public int getID() {
        return this.id;
    }

    abstract public void run();
}
