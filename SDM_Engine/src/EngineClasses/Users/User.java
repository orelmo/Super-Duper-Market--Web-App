package EngineClasses.Users;

import java.text.ParseException;
import java.util.List;

public class User {

    private static int idFactory = 1;

    protected int id;
    protected String name;
    protected eUserType userType;
    protected FinancialManager financialManager = new FinancialManager();

    public User() {}

    public User(User other){
        this.id = other.getId();
        this.name = other.getName();
        this.userType = other.getUserType();
        this.financialManager = other.financialManager;
    }

    public User(String userName, String userType) {
        this.id = idFactory++;
        this.name = userName;
        this.userType = eUserType.getUserType(userType);
    }

    public String getName() {
        return this.name;
    }

    public eUserType getUserType() {
        return this.userType;
    }

    public int getId() {
        return this.id;
    }

    public List<Transaction> getTransactionsList() {
        return this.financialManager.getTransactionsList();
    }

    public void deposit(String amount, String date) throws ParseException {
        this.financialManager.deposit(amount,date);
    }
}