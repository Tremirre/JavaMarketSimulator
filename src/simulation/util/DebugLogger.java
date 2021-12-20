package simulation.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugLogger {
    private static FileWriter file;
    public static void initLogger() {
        try {
            file = new FileWriter("log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void logMessage(String message) {
        try {
            file.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void quitLogger() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
