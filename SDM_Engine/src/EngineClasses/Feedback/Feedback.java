package EngineClasses.Feedback;

import SDMSystem.SDMSystemCustomer;

import java.util.Date;

public class Feedback {

    private int rate;
    private String message;
    private SDMSystemCustomer customer;
    private Date date;

    public Feedback(int rate, String message, SDMSystemCustomer customer, Date date) {
        this.rate = rate;
        this.message = message;
        this.customer = customer;
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public SDMSystemCustomer getCustomer() {
        return this.customer;
    }

    public int getRate() {
        return this.rate;
    }

    public String getMessage() {
        return this.message;
    }
}