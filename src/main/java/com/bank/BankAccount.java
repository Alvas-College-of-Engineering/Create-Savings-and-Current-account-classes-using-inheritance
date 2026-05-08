package com.bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BankAccount {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private final String accountNumber;
    private final String holderName;
    private final List<Transaction> transactions;
    private double balance;

    protected BankAccount(String accountNumber, String holderName, double openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = openingBalance;
        this.transactions = new ArrayList<>();
        record("Opened", openingBalance, "Account created");
    }

    public final Transaction deposit(double amount) {
        requirePositive(amount);
        balance += amount;
        return record("Deposit", amount, "Money credited successfully");
    }

    public final Transaction withdraw(double amount) {
        requirePositive(amount);
        if (!canWithdraw(amount)) {
            return record("Declined", amount, rejectionMessage(amount));
        }
        balance -= amount;
        return record("Withdrawal", amount, "Money debited successfully");
    }

    protected abstract boolean canWithdraw(double amount);

    protected abstract String rejectionMessage(double amount);

    public abstract String accountType();

    public abstract String ruleSummary();

    protected double balanceAfter(double amount) {
        return balance - amount;
    }

    private void requirePositive(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private Transaction record(String type, double amount, String note) {
        Transaction transaction = new Transaction(type, amount, balance, note, LocalDateTime.now().format(TIME_FORMAT));
        transactions.add(0, transaction);
        return transaction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
