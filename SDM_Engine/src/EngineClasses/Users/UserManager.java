package EngineClasses.Users;


import SDMSystem.SDMSystem;

import java.text.ParseException;
import java.util.*;

public class UserManager {
    private Map<String, User> users;
    private SDMSystem mySDMSystem;

    public UserManager(SDMSystem mySDMSystem){
        this.users = new HashMap<>();
        this.mySDMSystem = mySDMSystem;
    }

    public void add(String userName, String userType){
        User newUser = new User(userName, userType);
        this.users.put(userName, newUser);
        mySDMSystem.addUser(newUser);
    }

    public void remove(String userName){
        this.users.remove(userName);
    }

    public boolean isExist(String userName){
        return this.users.containsKey(userName);
    }

    public Map<String, User> getUsers() {
        return this.users;
    }

    public Set<String> getUserNames() {
        return this.users.keySet();
    }

    public List<Transaction> getTransactionsList(String username) {
        return this.users.get(username).getTransactionsList();

    }

    public void depositToUser(String username, String amount, String date) throws ParseException {
        User user = this.users.get(username);
        user.deposit(amount,date);
    }
}
