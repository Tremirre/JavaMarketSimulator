package simulation.util;

import java.util.Random;

/**
 * Common singleton class for providing randomness to all necessary compartments of the application.
 */
public final class RandomService {
    /**
     * Instance held as a singleton.
     */
    private static RandomService instance;
    /**
     * Final random generator to allow for repeatable random setup.
     */
    private final Random generator;

    /**
     * Parameterless constructor, sets a generator with seed if it is defined as non-negative in the Constants class,
     * or sets it without any specific seed otherwise.
     */
    private RandomService() {
        this.generator = Constants.RANDOM_SERVICE_SEED >= 0 ? new Random(Constants.RANDOM_SERVICE_SEED) : new Random();
    }

    /**
     * Generates random post code (in a polish __-___ fashion)
     * @return postal code as a String
     */
    public String yieldPostCode() {
        String[] randomValues = new String[5];
        for (int i = 0; i < randomValues.length; i++)
            randomValues[i] = String.valueOf(this.generator.nextInt(10));
        return randomValues[0] + randomValues[1] + '-' + randomValues[2] + randomValues[3] + randomValues[4];
    }

    /**
     * Uniformly generates a random integer
     * @param bound upper bound of the generation
     * @return random integer
     */
    public int yieldRandomInteger(int bound) {
        return this.generator.nextInt(bound);
    }

    /**
     * Uniformly generates a random double
     * @param upperBound upper bound of the generation
     * @return random double
     */
    public double yieldRandomNumber(double upperBound) {
        return this.generator.nextDouble() * upperBound;
    }

    /**
     * Generates a random double according to the normal distribution with given parameters.
     * @param standardDeviation SD of the normal distribution
     * @param mean Mean of the normal distribution
     * @return random double
     */
    public double yieldRandomGaussianNumber(double standardDeviation, double mean) {
        return this.generator.nextGaussian() * standardDeviation + mean;
    }

    /**
     * Generates a random string of a given length (consisting of small letters of latin alphabet)
     * @param length length of the string
     * @return random string
     */
    public String yieldRandomString(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((char) (this.generator.nextInt(26) + 97));
        }
        return result.toString();
    }

    /**
     * Samples a random element from any array of non-primitive objects
     * @param array array of non-primitive objects to be sampled from
     * @return a random object from that array (as an Object, casting may be required)
     */
    public Object sampleElement(Object[] array) {
        if (array.length == 0)
            return null;
        return array[this.generator.nextInt(array.length)];
    }

    /**
     * Yields a random date in form of a string. (Date ranges from 1980 to 2021)
     * It generates a day independently of the month, hence it is impossible to get a date
     * with day later than 28th (as exclude possibility of generating an impossible date).
     * @return a random date in YYYY-MM-DD form.
     */
    public String yieldDate() {
        var month = this.generator.nextInt(12) + 1;
        var day = this.generator.nextInt(28) + 1;
        var year = this.generator.nextInt(41) + 1980;
        String monthD = month < 10 ? "0" : "";
        monthD += String.valueOf(month);
        String dayD = day < 10 ? "0" : "";
        dayD += String.valueOf(day);
        return String.valueOf(year) + '-' + monthD + '-' + dayD;
    }

    /**
     * Returns a RandomService instance.
     * It is synchronised as to ensure that only one thread at a time may create new Random Service instance.
     * @return RandomService instance.
     */
    public synchronized static RandomService getInstance() {
        if (instance == null) {
            instance = new RandomService();
        }
        return instance;
    }
}