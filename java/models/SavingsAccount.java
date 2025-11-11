package models;

import java.time.LocalDateTime;

/**
 * Savings account with interest.
 */
public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private double interestRate; // annual rate in percent, e.g. 3.5

    public SavingsAccount(String customerId, double initialDeposit, double interestRate) {
        super(customerId, initialDeposit);
        this.interestRate = interestRate;
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be > 0");
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be > 0");
        if (amount > balance) throw new InsufficientFundsException("Insufficient funds");
        balance -= amount;
    }

    /**
     * Apply interest for specified months, simple interest calculation for demonstration.
     */
    public void applyInterestMonths(int months) {
        if (months <= 0) return;
        double monthlyRate = (interestRate / 100.0) / 12.0;
        balance += balance * monthlyRate * months;
    }
}
