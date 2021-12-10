package bst;

import simulation.market.CurrenciesMarket;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var market = new CurrenciesMarket("Super cool", 0.04, 0.05);
        System.out.println(market.getName());
    }
}