package simulation.random;

public class RandomDataGenerator {
    private static RandomDataGenerator instance;

    private RandomDataGenerator() {

    }

    public String yieldCountry() {
        return "";
    }

    public static RandomDataGenerator getInstance() {
        if (instance == null) {
            instance = new RandomDataGenerator();
        }
        return instance;
    }
}
