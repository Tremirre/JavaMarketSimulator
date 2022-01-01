package simulation.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GlobalInvestorLock {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
