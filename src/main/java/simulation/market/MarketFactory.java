package main.java.simulation.market;

public interface MarketFactory {
    Market createMarket(MarketType type);
}
