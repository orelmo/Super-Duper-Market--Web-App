package EngineClasses.Users;

import EngineClasses.Interfaces.Containable;

import java.util.ArrayList;
import java.util.List;

public class Transactions {

    private List<Transaction> transactionList = new ArrayList<>();

    public void add(Transaction newTransaction){
        this.transactionList.add(newTransaction);
    }

    public List<Transaction> getIterable() {
        return this.transactionList;
    }
}