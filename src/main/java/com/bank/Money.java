package com.bank;

import java.text.NumberFormat;
import java.util.Locale;

public final class Money {
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    private Money() {
    }

    public static String format(double amount) {
        return FORMATTER.format(amount);
    }
}
