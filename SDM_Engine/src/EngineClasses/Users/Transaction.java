package EngineClasses.Users;

import java.util.Date;

public class Transaction {
    private eTransactionType transactionType;
    private Date transactionDate;
    private float transactionAmount;
    private float preBalance;
    private float postBalance;

    public Transaction(eTransactionType type, Date date, float transactionAmount,float preBalance, float postBalance){
        this.transactionType = type;
        this.transactionDate = date;
        this.transactionAmount = transactionAmount;
        this.preBalance = preBalance;
        this.postBalance = postBalance;
    }
}
