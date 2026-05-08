package com.bank;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BankWebApplication {
    private static final BankService BANK = new BankService();

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getProperty("port", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", BankWebApplication::handleHome);
        server.createContext("/transaction", BankWebApplication::handleTransaction);
        server.setExecutor(null);
        server.start();
        System.out.println("Bank Account Management System running at http://localhost:" + port);
    }

    private static void handleHome(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            redirect(exchange, "/");
            return;
        }
        respond(exchange, PageRenderer.render(BANK, null, false));
    }

    private static void handleTransaction(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            redirect(exchange, "/");
            return;
        }

        String message;
        boolean error = false;
        try {
            Map<String, String> form = parseForm(exchange);
            String accountNumber = form.getOrDefault("accountNumber", "");
            String action = form.getOrDefault("action", "");
            double amount = Double.parseDouble(form.getOrDefault("amount", "0"));
            Transaction transaction = BANK.transact(accountNumber, action, amount);
            message = transaction.type() + " of " + Money.format(transaction.amount()) + ": " + transaction.note();
            error = transaction.declined();
        } catch (Exception ex) {
            message = ex.getMessage() == null ? "Transaction could not be completed." : ex.getMessage();
            error = true;
        }
        respond(exchange, PageRenderer.render(BANK, message, error));
    }

    private static Map<String, String> parseForm(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> values = new HashMap<>();
        for (String pair : body.split("&")) {
            if (pair.isBlank()) {
                continue;
            }
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length > 1 ? decode(parts[1]) : "";
            values.put(key, value);
        }
        return values;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static void respond(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(303, -1);
        exchange.close();
    }
}
