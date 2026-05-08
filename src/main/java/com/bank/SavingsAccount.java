package com.bank;

public class SavingsAccount extends BankAccount {
    private final double minimumBalance;

    public SavingsAccount(String accountNumber, String holderName, double openingBalance, double minimumBalance) {
        super(accountNumber, holderName, openingBalance);
        this.minimumBalance = minimumBalance;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return balanceAfter(amount) >= minimumBalance;
    }

    @Override
    protected String rejectionMessage(double amount) {
        return "Savings account must keep minimum balance of " + Money.format(minimumBalance) + ".";
    }

    @Override
    public String accountType() {
        return "Savings Account";
    }

    @Override
    public String ruleSummary() {
        return "Minimum balance: " + Money.format(minimumBalance);
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }
}
