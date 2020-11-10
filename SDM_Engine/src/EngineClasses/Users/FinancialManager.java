package EngineClasses.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class FinancialManager {
    private float balance;
    private Transactions transactionHistory;

    public FinancialManager() {
        this.balance = 0;
        this.transactionHistory = new Transactions();
    }

    public List<Transaction> getTransactionsList() {
        return this.transactionHistory.getIterable();
    }

    public void  deposit(String amount, String stringDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(stringDate);
        transactionHistory.add(new Transaction(eTransactionType.Deposit, date, Float.parseFloat(amount),
                this.balance,this.balance+Float.parseFloat(amount)));
        this.balance += Float.parseFloat(amount);
    }

    public void withdraw(float payment, Date transactionDate) {
        transactionHistory.add(new Transaction(eTransactionType.Payment, transactionDate, payment ,this.balance, this.balance - payment));
        this.balance -= payment;
    }

    public void getPayment(float payment, Date transactionDate) {
        this.transactionHistory.add(new Transaction(eTransactionType.Receive, transactionDate, payment, this.balance, this.balance + payment));
        this.balance += payment;
    }
}