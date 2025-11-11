package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction record for deposits, withdrawals and transfers
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER }

    private final String transactionId;
    private final Type type;
    private final String fromAccountId; // nullable for deposits
    private final String toAccountId;   // nullable for withdrawals
    private final double amount;
    private final LocalDateTime timestamp;
    private final String note;

    public Transaction(Type type, String fromAccountId, String toAccountId, double amount, String note) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.note = note;
    }

    public String getTransactionId() { return transactionId; }
    public Type getType() { return type; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, type=%s, from=%s, to=%s, amount=%.2f, time=%s, note='%s'}",
                transactionId, type, fromAccountId, toAccountId, amount, timestamp, note);
    }
}
