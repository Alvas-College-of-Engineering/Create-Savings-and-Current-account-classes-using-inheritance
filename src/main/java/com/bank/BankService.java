package com.bank;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankService {
    private final Map<String, BankAccount> accounts = new LinkedHashMap<>();

    public BankService() {
        addAccount(new SavingsAccount("SAV-1001", "Aarav Sharma", 18000, 5000));
        addAccount(new CurrentAccount("CUR-2001", "Meera Traders", 32000, 25000));
        account("SAV-1001").deposit(2500);
        account("SAV-1001").withdraw(4000);
        account("SAV-1001").withdraw(13000);
        account("CUR-2001").withdraw(47000);
        account("CUR-2001").deposit(8500);
    }

    public Collection<BankAccount> accounts() {
        return accounts.values();
    }

    public BankAccount account(String accountNumber) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Unknown account.");
        }
        return account;
    }

    public Transaction transact(String accountNumber, String action, double amount) {
        BankAccount account = account(accountNumber);
        return switch (action) {
            case "deposit" -> account.deposit(amount);
            case "withdraw" -> account.withdraw(amount);
            default -> throw new IllegalArgumentException("Choose deposit or withdraw.");
        };
    }

    private void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }
}
