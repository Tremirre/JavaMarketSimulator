package bst;

import simulation.asset.AssetManager;
import simulation.asset.AssetType;
import simulation.market.CurrenciesMarket;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var market = new CurrenciesMarket("Super cool", 0.04, 0.05);
        System.out.println(market.getName());
        var currencies = market.getAvailableAssetTypes();
        for (var cur : currencies)
        {
            System.out.println(AssetManager.getInstance().doesAssetExist(cur, AssetType.currency));
        }
    }
}