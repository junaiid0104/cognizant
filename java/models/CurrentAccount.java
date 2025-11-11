package models;

/**
 * Current account (may have overdraft facility).
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private double overdraftLimit;

    public CurrentAccount(String customerId, double initialDeposit, double overdraftLimit) {
        super(customerId, initialDeposit);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be > 0");
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("Withdraw amount must be > 0");
        if (balance - amount < -overdraftLimit) throw new InsufficientFundsException("Overdraft limit exceeded");
        balance -= amount;
    }
}
