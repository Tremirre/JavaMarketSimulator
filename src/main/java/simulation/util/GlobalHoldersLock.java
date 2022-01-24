package simulation.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Static class for read-write lock for ensuring the proper concurrent working and modification.
 */
public class GlobalHoldersLock {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void readLock() {
        lock.readLock().lock();
    }

    public static void readUnlock() {
        lock.readLock().unlock();
    }

    public static void writeLock() {
        lock.writeLock().lock();
    }

    public static void writeUnlock() {
        lock.writeLock().unlock();
    }
}
