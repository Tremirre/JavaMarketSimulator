package bst;

import simulation.holders.Address;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            var address = Address.getRandomAddress();
            address.print();
            System.out.println("---------------------------");
        }
    }
}