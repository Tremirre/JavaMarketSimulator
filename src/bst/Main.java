package bst;

import simulation.holders.Company;
import simulation.holders.HolderType;
import simulation.holders.RandomHolderFactory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Company company = (Company) new RandomHolderFactory().createHolder(HolderType.COMPANY);
        company.print();
    }
}