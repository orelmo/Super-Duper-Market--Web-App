package FeedbackDTO;

import java.util.Date;

public class FeedbackDTO {

    private String customerName;
    private Date date;
    private String storeName;
    private int rate;
    private String message;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}