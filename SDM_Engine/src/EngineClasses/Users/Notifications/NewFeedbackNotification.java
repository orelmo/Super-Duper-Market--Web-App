package EngineClasses.Users.Notifications;

import EngineClasses.Feedback.Feedback;
import SDMSystem.SDMSystemCustomer;

public class NewFeedbackNotification implements Notification {
    private int rate;
    private String message;
    private eNotificationType notificationType;
    private String customerName;

    public NewFeedbackNotification(Feedback feedback) {
        this.rate = feedback.getRate();
        this.message = feedback.getMessage();
        this.notificationType = eNotificationType.NEW_FEEDBACK;
        this.customerName = feedback.getCustomer().getName();
    }
}