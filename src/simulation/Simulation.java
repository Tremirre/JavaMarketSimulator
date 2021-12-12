package simulation;

import simulation.holders.AssetHolder;
import simulation.market.Market;
import java.util.ArrayList;

public class Simulation {
    private ArrayList<Market> markets;
    private ArrayList<AssetHolder> assetHolders;
    private SimulationConfig simConfig;

    public Simulation() {
        this.simConfig = new SimulationConfig();

    }

    public void runSimulationDay() {

    }
}
