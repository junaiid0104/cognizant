package utils;

import services.Bank;
import models.Account;
import models.Customer;

public class SeedData {
    public static void seed(Bank bank) {
        try {
            Customer c1 = bank.createCustomer("Aisha Khan", "aisha@example.com", "9990011111");
            Customer c2 = bank.createCustomer("Rahul Verma", "rahul@example.com", "8880022222");

            Account a1 = bank.createSavingsAccount(c1.getCustomerId(), 5000.0, 3.5);
            Account a2 = bank.createCurrentAccount(c1.getCustomerId(), 2000.0, 1000.0);

            Account a3 = bank.createSavingsAccount(c2.getCustomerId(), 15000.0, 4.0);

            bank.deposit(a2.getAccountId(), 1000.0, "Top-up");
            bank.transfer(a1.getAccountId(), a2.getAccountId(), 500.0, "Internal transfer");
        } catch (Exception e) {
            System.err.println("Seed data error: " + e.getMessage());
        }
    }
}
