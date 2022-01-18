package application.util.format;

import application.driver.MainController;
import simulation.core.Simulation;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimulationRunner extends Thread {
    private ReentrantReadWriteLock runLock = new ReentrantReadWriteLock();
    private final MainController controller;
    private final Simulation simulation;
    private boolean running = true;
    private boolean paused = false;

    public SimulationRunner(MainController controller, Simulation simulation) {
        this.controller = controller;
        this.simulation = simulation;
    }

    public void run() {
        if (!this.simulation.hasStarted())
            this.simulation.start();
        while(running) {
            this.runLock.readLock().lock();
            this.simulation.runSimulationDay();
            this.runLock.readLock().unlock();
            this.controller.refreshSimulationData();
        }
        this.simulation.stop();
    }

    public void pause() {
        this.paused = true;
        this.runLock.writeLock().lock();
    }

    public void unpause() {
        this.paused = false;
        this.runLock.writeLock().unlock();
    }

    public void stopRunning() {
        if (this.paused)
            this.unpause();
        this.running = false;
    }
}
