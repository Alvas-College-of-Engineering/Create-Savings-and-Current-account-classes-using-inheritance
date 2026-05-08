package com.bank;

public final class PageRenderer {
    private PageRenderer() {
    }

    public static String render(BankService bank, String message, boolean error) {
        StringBuilder accountCards = new StringBuilder();
        StringBuilder accountOptions = new StringBuilder();
        for (BankAccount account : bank.accounts()) {
            accountCards.append(accountCard(account));
            accountOptions.append("<option value=\"").append(escape(account.getAccountNumber())).append("\">")
                    .append(escape(account.accountType())).append(" - ")
                    .append(escape(account.getHolderName())).append("</option>");
        }

        String notice = "";
        if (message != null && !message.isBlank()) {
            notice = "<section class=\"notice " + (error ? "error" : "success") + "\">" + escape(message) + "</section>";
        }

        return """
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Bank Account Management System</title>
                    <style>
                """ + styles() + """
                    </style>
                </head>
                <body>
                    <main class="shell">
                        <section class="hero">
                            <div>
                                <p class="eyebrow">Java Dynamic Web Project</p>
                                <h1>Bank Account Management System</h1>
                                <p class="subtitle">Savings accounts protect a minimum balance. Current accounts support overdraft withdrawals.</p>
                            </div>
                            <div class="summary">
                                <span>2 account types</span>
                                <strong>Inheritance Demo</strong>
                            </div>
                        </section>
                """ + notice + """
                        <section class="workspace">
                            <form class="panel transaction" method="post" action="/transaction">
                                <h2>New Transaction</h2>
                                <label>
                                    Account
                                    <select name="accountNumber">""" + accountOptions + """
                                    </select>
                                </label>
                                <label>
                                    Action
                                    <select name="action">
                                        <option value="deposit">Deposit</option>
                                        <option value="withdraw">Withdraw</option>
                                    </select>
                                </label>
                                <label>
                                    Amount
                                    <input name="amount" type="number" min="1" step="100" value="1000" required>
                                </label>
                                <button type="submit">Process Transaction</button>
                            </form>
                            <section class="accounts">
                                """ + accountCards + """
                            </section>
                        </section>
                    </main>
                </body>
                </html>
                """;
    }

    private static String accountCard(BankAccount account) {
        StringBuilder rows = new StringBuilder();
        for (Transaction transaction : account.getTransactions()) {
            rows.append("<li class=\"").append(transaction.declined() ? "declined" : "").append("\">")
                    .append("<span><strong>").append(escape(transaction.type())).append("</strong>")
                    .append("<small>").append(escape(transaction.timestamp())).append("</small></span>")
                    .append("<span>").append(escape(Money.format(transaction.amount()))).append("</span>")
                    .append("<em>").append(escape(transaction.note())).append("</em>")
                    .append("</li>");
        }

        return """
                <article class="account-card">
                    <div class="card-top">
                        <span class="type">""" + escape(account.accountType()) + """
                        </span>
                        <span class="number">""" + escape(account.getAccountNumber()) + """
                        </span>
                    </div>
                    <h2>""" + escape(account.getHolderName()) + """
                    </h2>
                    <p class="balance">""" + escape(Money.format(account.getBalance())) + """
                    </p>
                    <p class="rule">""" + escape(account.ruleSummary()) + """
                    </p>
                    <ul class="history">
                        """ + rows + """
                    </ul>
                </article>
                """;
    }

    private static String styles() {
        return """
                * { box-sizing: border-box; }
                body {
                    margin: 0;
                    min-height: 100vh;
                    font-family: Arial, Helvetica, sans-serif;
                    color: #17211b;
                    background: #eef3f1;
                }
                .shell { width: min(1120px, calc(100% - 32px)); margin: 0 auto; padding: 32px 0; }
                .hero {
                    display: grid;
                    grid-template-columns: 1fr auto;
                    gap: 24px;
                    align-items: end;
                    padding: 34px;
                    color: white;
                    background: linear-gradient(135deg, #0f3d2e, #16634a 54%, #c28b2c);
                    border-radius: 8px;
                    box-shadow: 0 20px 60px rgba(20, 61, 47, .18);
                }
                .eyebrow { margin: 0 0 12px; text-transform: uppercase; font-size: 12px; letter-spacing: 2px; opacity: .84; }
                h1 { margin: 0; font-size: 42px; line-height: 1.05; letter-spacing: 0; }
                .subtitle { margin: 14px 0 0; max-width: 650px; color: rgba(255,255,255,.86); font-size: 17px; line-height: 1.55; }
                .summary {
                    min-width: 210px;
                    padding: 18px;
                    background: rgba(255,255,255,.12);
                    border: 1px solid rgba(255,255,255,.24);
                    border-radius: 8px;
                }
                .summary span { display: block; font-size: 13px; opacity: .82; }
                .summary strong { display: block; margin-top: 8px; font-size: 24px; }
                .notice { margin: 18px 0; padding: 14px 16px; border-radius: 8px; font-weight: 700; }
                .notice.success { color: #145033; background: #dff4e8; border: 1px solid #a9dfbf; }
                .notice.error { color: #7a251c; background: #fde7e2; border: 1px solid #f1b3a7; }
                .workspace { display: grid; grid-template-columns: 320px 1fr; gap: 20px; margin-top: 20px; align-items: start; }
                .panel, .account-card { background: #ffffff; border: 1px solid #dce5e1; border-radius: 8px; box-shadow: 0 12px 32px rgba(25, 39, 33, .08); }
                .transaction { padding: 20px; position: sticky; top: 18px; }
                .transaction h2 { margin: 0 0 18px; font-size: 22px; }
                label { display: grid; gap: 8px; margin-bottom: 14px; color: #52615a; font-size: 13px; font-weight: 700; }
                select, input {
                    width: 100%;
                    min-height: 44px;
                    border: 1px solid #cbd8d2;
                    border-radius: 8px;
                    padding: 0 12px;
                    font-size: 15px;
                    color: #17211b;
                    background: #fbfdfc;
                }
                button {
                    width: 100%;
                    min-height: 46px;
                    border: 0;
                    border-radius: 8px;
                    background: #c28b2c;
                    color: #11150f;
                    font-size: 15px;
                    font-weight: 800;
                    cursor: pointer;
                }
                button:hover { background: #d49b37; }
                .accounts { display: grid; gap: 18px; }
                .account-card { padding: 20px; overflow: hidden; }
                .card-top { display: flex; justify-content: space-between; gap: 12px; align-items: center; }
                .type { color: #17624a; background: #e4f2ec; padding: 7px 10px; border-radius: 999px; font-size: 12px; font-weight: 800; }
                .number { color: #65746d; font-size: 13px; font-weight: 800; }
                .account-card h2 { margin: 18px 0 8px; font-size: 24px; }
                .balance { margin: 0; font-size: 38px; font-weight: 900; letter-spacing: 0; }
                .rule { margin: 8px 0 18px; color: #65746d; font-weight: 700; }
                .history { list-style: none; padding: 0; margin: 0; display: grid; gap: 10px; }
                .history li {
                    display: grid;
                    grid-template-columns: 1fr auto;
                    gap: 4px 14px;
                    padding: 12px;
                    border-radius: 8px;
                    background: #f7faf8;
                    border: 1px solid #e7eeea;
                }
                .history li.declined { background: #fff4f1; border-color: #f2c4ba; }
                .history strong { display: block; }
                .history small { display: block; color: #728079; margin-top: 3px; }
                .history em { grid-column: 1 / -1; color: #65746d; font-style: normal; font-size: 13px; }
                @media (max-width: 820px) {
                    .shell { width: min(100% - 20px, 1120px); padding: 10px 0 24px; }
                    .hero, .workspace { grid-template-columns: 1fr; }
                    .hero { padding: 24px; }
                    h1 { font-size: 32px; }
                    .transaction { position: static; }
                    .balance { font-size: 30px; }
                }
                """;
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
