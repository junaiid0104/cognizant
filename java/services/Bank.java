package services;

import models.*;
import exceptions.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bank service: manages customers, accounts, transactions.
 * In-memory storage with optional serialization persistence.
 */
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    // Simple in-memory 'db'
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());

    // ----- Customer operations -----
    public Customer createCustomer(String name, String email, String phone) {
        Customer c = new Customer(name, email, phone);
        customers.put(c.getCustomerId(), c);
        return c;
    }

    public Customer getCustomer(String customerId) throws EntityNotFoundException {
        Customer c = customers.get(customerId);
        if (c == null) throw new EntityNotFoundException("Customer not found: " + customerId);
        return c;
    }

    public List<Customer> listCustomers() {
        return new ArrayList<>(customers.values());
    }

    // ----- Account operations -----
    public Account createSavingsAccount(String customerId, double initialDeposit, double annualInterestRate) throws EntityNotFoundException {
        requireCustomerExists(customerId);
        SavingsAccount acc = new SavingsAccount(customerId, initialDeposit, annualInterestRate);
        accounts.put(acc.getAccountId(), acc);
        if (initialDeposit > 0) recordTransaction(Transaction.Type.DEPOSIT, null, acc.getAccountId(), initialDeposit, "Initial deposit");
        return acc;
    }

    public Account createCurrentAccount(String customerId, double initialDeposit, double overdraftLimit) throws EntityNotFoundException {
        requireCustomerExists(customerId);
        CurrentAccount acc = new CurrentAccount(customerId, initialDeposit, overdraftLimit);
        accounts.put(acc.getAccountId(), acc);
        if (initialDeposit > 0) recordTransaction(Transaction.Type.DEPOSIT, null, acc.getAccountId(), initialDeposit, "Initial deposit");
        return acc;
    }

    public Account getAccount(String accountId) throws EntityNotFoundException {
        Account acc = accounts.get(accountId);
        if (acc == null) throw new EntityNotFoundException("Account not found: " + accountId);
        return acc;
    }

    public List<Account> listAccountsForCustomer(String customerId) {
        return accounts.values().stream()
                .filter(a -> a.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    // deposit
    public Transaction deposit(String accountId, double amount, String note) throws EntityNotFoundException {
        Account acc = getAccount(accountId);
        acc.deposit(amount);
        Transaction t = recordTransaction(Transaction.Type.DEPOSIT, null, accountId, amount, note);
        return t;
    }

    // withdraw
    public Transaction withdraw(String accountId, double amount, String note) throws EntityNotFoundException, InsufficientFundsException {
        Account acc = getAccount(accountId);
        acc.withdraw(amount);
        Transaction t = recordTransaction(Transaction.Type.WITHDRAWAL, accountId, null, amount, note);
        return t;
    }

    // transfer
    public Transaction transfer(String fromAccountId, String toAccountId, double amount, String note) throws EntityNotFoundException, InsufficientFundsException {
        if (fromAccountId.equals(toAccountId)) throw new IllegalArgumentException("Source and destination cannot be same");
        Account from = getAccount(fromAccountId);
        Account to = getAccount(toAccountId);

        // perform in order: withdraw then deposit
        from.withdraw(amount);
        to.deposit(amount);

        Transaction t = recordTransaction(Transaction.Type.TRANSFER, fromAccountId, toAccountId, amount, note);
        return t;
    }

    // record transaction
    private Transaction recordTransaction(Transaction.Type type, String from, String to, double amount, String note) {
        Transaction t = new Transaction(type, from, to, amount, note == null ? "" : note);
        transactions.add(t);
        return t;
    }

    public List<Transaction> listTransactionsForAccount(String accountId) {
        return transactions.stream()
                .filter(t -> accountId.equals(t.getFromAccountId()) || accountId.equals(t.getToAccountId()))
                .collect(Collectors.toList());
    }

    public List<Transaction> listAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // persistence: save/load the entire bank to a file
    public void saveToFile(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this);
        }
    }

    public static Bank loadFromFile(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Object obj = ois.readObject();
            if (obj instanceof Bank) return (Bank) obj;
            throw new IOException("Invalid file content");
        }
    }

    // helper
    private void requireCustomerExists(String customerId) throws EntityNotFoundException {
        if (!customers.containsKey(customerId)) throw new EntityNotFoundException("Customer not found: " + customerId);
    }
}
