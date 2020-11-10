package EngineClasses.Users.Notifications;

import EngineClasses.Order.Order;

public class NewOrderNotification implements Notification {
    private int orderId;
    private String storeName;
    private String customerUsername;
    private int numberOfDifferentItems;
    private float itemsPrice;
    private float deliveryPrice;
    private eNotificationType notificationType;

    public NewOrderNotification(Order order, String storeName) {
        this.orderId = order.getId();
        this.storeName = storeName;
        this.customerUsername = order.getCustomer().getName();
        this.numberOfDifferentItems = order.getOrderItems().size();
        this.itemsPrice = order.getItemsPrice();
        this.deliveryPrice = order.getDeliveryPrice();
        this.notificationType = eNotificationType.NEW_ORDER;
    }
}
