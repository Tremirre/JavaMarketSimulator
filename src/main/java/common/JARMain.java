package common;

import application.driver.Application;
import simulation.SimulationMain;

/**
 * Main class to be used in the jar file.
 * Runs only the console version if the flag -s is passed.
 */
public class JARMain {
    public static void main(String[] args) {
        if (args.length == 0)
            Application.main(args);
        else if (args[0].equals("-s"))
            SimulationMain.main(args);
        else {
            System.out.println("Unknown argument passed: " + args[0]);
            System.out.println("For running the GUI app run without any arguments");
            System.out.println("For running the console app run with -s flag");
        }
    }
}
