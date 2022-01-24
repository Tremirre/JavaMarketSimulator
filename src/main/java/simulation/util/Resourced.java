package simulation.util;

/**
 * Interface for all necessary classes that use random data generation (e.g. random factories).
 * All of them share a common ResourceHolder instance.
 * @see ResourceHolder
 */
public interface Resourced {
    ResourceHolder resourceHolder = new ResourceHolder();
}
