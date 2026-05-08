package com.bank;

public class CurrentAccount extends BankAccount {
    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, String holderName, double openingBalance, double overdraftLimit) {
        super(accountNumber, holderName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return balanceAfter(amount) >= -overdraftLimit;
    }

    @Override
    protected String rejectionMessage(double amount) {
        return "Current account overdraft limit is " + Money.format(overdraftLimit) + ".";
    }

    @Override
    public String accountType() {
        return "Current Account";
    }

    @Override
    public String ruleSummary() {
        return "Overdraft facility: " + Money.format(overdraftLimit);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}
