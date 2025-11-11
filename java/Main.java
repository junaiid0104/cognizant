import services.Bank;
import models.*;
import exceptions.*;
import utils.SeedData;

import java.util.List;
import java.util.Scanner;

/**
 * Demo CLI for bank module
 */
public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // seed some sample data
        SeedData.seed(bank);

        Scanner sc = new Scanner(System.in);
        printWelcome();
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Choose option: ");
            String opt = sc.nextLine().trim();
            try {
                switch (opt) {
                    case "1": // list customers
                        List<Customer> customers = bank.listCustomers();
                        System.out.println("Customers:");
                        customers.forEach(System.out::println);
                        break;
                    case "2": // create customer
                        System.out.print("Name: "); String name = sc.nextLine();
                        System.out.print("Email: "); String email = sc.nextLine();
                        System.out.print("Phone: "); String phone = sc.nextLine();
                        Customer c = bank.createCustomer(name, email, phone);
                        System.out.println("Created: " + c);
                        break;
                    case "3": // create account
                        System.out.print("CustomerId: "); String cid = sc.nextLine();
                        System.out.print("Type (savings/current): "); String type = sc.nextLine().trim().toLowerCase();
                        System.out.print("Initial deposit: "); double init = Double.parseDouble(sc.nextLine());
                        if ("savings".equals(type)) {
                            System.out.print("Interest rate (annual %): "); double rate = Double.parseDouble(sc.nextLine());
                            Account sa = bank.createSavingsAccount(cid, init, rate);
                            System.out.println("Created: " + sa);
                        } else {
                            System.out.print("Overdraft limit: "); double od = Double.parseDouble(sc.nextLine());
                            Account ca = bank.createCurrentAccount(cid, init, od);
                            System.out.println("Created: " + ca);
                        }
                        break;
                    case "4": // deposit
                        System.out.print("AccountId: "); String aid = sc.nextLine();
                        System.out.print("Amount: "); double damt = Double.parseDouble(sc.nextLine());
                        bank.deposit(aid, damt, "Manual deposit");
                        System.out.println("Deposit successful");
                        break;
                    case "5": // withdraw
                        System.out.print("AccountId: "); String wa = sc.nextLine();
                        System.out.print("Amount: "); double wamt = Double.parseDouble(sc.nextLine());
                        bank.withdraw(wa, wamt, "Manual withdrawal");
                        System.out.println("Withdrawal successful");
                        break;
                    case "6": // transfer
                        System.out.print("From account: "); String fa = sc.nextLine();
                        System.out.print("To account: "); String ta = sc.nextLine();
                        System.out.print("Amount: "); double tamt = Double.parseDouble(sc.nextLine());
                        bank.transfer(fa, ta, tamt, "Manual transfer");
                        System.out.println("Transfer successful");
                        break;
                    case "7": // list transactions for account
                        System.out.print("AccountId: "); String txa = sc.nextLine();
                        List<Transaction> txs = bank.listTransactionsForAccount(txa);
                        System.out.println("Transactions:");
                        txs.forEach(System.out::println);
                        break;
                    case "8": // save bank to file
                        System.out.print("Save path (e.g. bank.db): "); String path = sc.nextLine();
                        bank.saveToFile(path);
                        System.out.println("Saved to " + path);
                        break;
                    case "9": // load from file
                        System.out.print("Load path: "); String loadPath = sc.nextLine();
                        Bank loaded = Bank.loadFromFile(loadPath);
                        bank = loaded;
                        System.out.println("Loaded bank from " + loadPath);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown option.");
                }
            } catch (EntityNotFoundException | InsufficientFundsException ex) {
                System.err.println("Operation failed: " + ex.getMessage());
            } catch (NumberFormatException nfe) {
                System.err.println("Invalid number input.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        sc.close();
        System.out.println("Goodbye!");
    }

    private static void printWelcome() {
        System.out.println("=== Simple Bank Management System ===");
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1 - List customers");
        System.out.println("2 - Create customer");
        System.out.println("3 - Create account (savings/current)");
        System.out.println("4 - Deposit");
        System.out.println("5 - Withdraw");
        System.out.println("6 - Transfer");
        System.out.println("7 - List transactions for account");
        System.out.println("8 - Save bank to file");
        System.out.println("9 - Load bank from file");
        System.out.println("0 - Exit");
    }
}
