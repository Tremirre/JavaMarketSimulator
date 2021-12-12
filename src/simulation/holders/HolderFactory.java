package simulation.holders;

import java.io.IOException;

public abstract class HolderFactory {
    private static int id = 0;
    abstract AssetHolder createHolder(HolderType type) throws IOException;

    public static int fetchID() {
        return id++;
    }
}
