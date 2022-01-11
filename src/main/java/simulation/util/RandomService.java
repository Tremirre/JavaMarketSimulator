package simulation.util;

import java.util.Random;

public final class RandomService {
    private static RandomService instance;
    private final Random generator;
    private static final int SEED = 1;

    private RandomService() {
        this.generator = SEED >= 0 ? new Random(SEED) : new Random();
    }

    public String yieldPostCode() {
        String[] randomValues = new String[5];
        for (int i = 0; i < randomValues.length; i++)
            randomValues[i] = String.valueOf(this.generator.nextInt(10));
        return randomValues[0] + randomValues[1] + '-' + randomValues[2] + randomValues[3] + randomValues[4];
    }

    public int yieldRandomInteger(int bound) {
        return this.generator.nextInt(bound);
    }

    public double yieldRandomNumber(double upperBound) {
        return this.generator.nextDouble() * upperBound;
    }

    public double yieldRandomGaussianNumber(double standardDeviation, double mean) {
        return this.generator.nextGaussian() * standardDeviation + mean;
    }

    public String yieldRandomString(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((char) (this.generator.nextInt(26) + 97));
        }
        return result.toString();
    }

    public Object sampleElement(Object[] array) {
        if (array.length == 0)
            return null;
        return array[this.generator.nextInt(array.length)];
    }

    public String yieldDate() {
        var month = this.generator.nextInt(12) + 1;
        var day = this.generator.nextInt(29) + 1;
        var year = this.generator.nextInt(41) + 1980;
        String monthD = month < 10 ? "0" : "";
        monthD += String.valueOf(month);
        String dayD = day < 10 ? "0" : "";
        dayD += String.valueOf(day);
        return String.valueOf(year) + '-' + monthD + '-' + dayD;
    }

    public synchronized static RandomService getInstance() {
        if (instance == null) {
            instance = new RandomService();
        }
        return instance;
    }
}