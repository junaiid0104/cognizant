package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract account class. Concrete account types extend this.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String accountId;
    protected final String customerId;
    protected double balance;
    protected final LocalDateTime createdAt;
    protected boolean isActive;

    public Account(String customerId, double initialDeposit) {
        this.accountId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.balance = initialDeposit;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public String getAccountId() { return accountId; }
    public String getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
    public void close() { isActive = false; }

    public abstract void deposit(double amount) throws IllegalArgumentException;
    public abstract void withdraw(double amount) throws IllegalArgumentException, InsufficientFundsException;

    @Override
    public String toString() {
        return String.format("%s{accountId='%s', customerId='%s', balance=%.2f, createdAt=%s, active=%s}",
                this.getClass().getSimpleName(), accountId, customerId, balance, createdAt, isActive);
    }
}
