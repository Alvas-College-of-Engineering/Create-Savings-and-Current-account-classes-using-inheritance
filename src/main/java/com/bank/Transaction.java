package com.bank;

public record Transaction(String type, double amount, double resultingBalance, String note, String timestamp) {
    public boolean declined() {
        return "Declined".equalsIgnoreCase(type);
    }
}
