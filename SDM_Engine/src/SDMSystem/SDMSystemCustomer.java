package SDMSystem;

import EngineClasses.Customer.Customer;
import EngineClasses.Order.Order;
import EngineClasses.Store.Store;
import EngineClasses.Users.User;

import java.util.Date;
import java.util.List;

public class SDMSystemCustomer extends Customer {
    private int numberOfOrders;
    private float avgOrdersItemsPrice;
    private float avgOrdersDeliveryPrice;
    private SDMZonesOrderHistory zonesOrderHistory = new SDMZonesOrderHistory();

    public SDMSystemCustomer() {
    }

    public SDMSystemCustomer(User newUser) {
        super(newUser);
        this.numberOfOrders = 0;
        this.avgOrdersDeliveryPrice = 0;
        this.avgOrdersItemsPrice = 0;
    }

    public void addOrderToOrderHistory(String zoneName, Order newOrder) {
        this.zonesOrderHistory.add(zoneName,newOrder);
    }

    public float getAvgOrdersItemsPrice() {
        return this.avgOrdersItemsPrice;
    }

    public float getAvgOrdersDeliveryPrice() {
        return this.avgOrdersDeliveryPrice;
    }

    public int getNumberOfOrders() {
        return this.numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public void setAvgOrdersItemsPrice(float avgOrdersItemsPrice) {
        this.avgOrdersItemsPrice = avgOrdersItemsPrice;
    }

    public void setAvgOrdersDeliveryPrice(float avgOrdersDeliveryPrice) {
        this.avgOrdersDeliveryPrice = avgOrdersDeliveryPrice;
    }

    public void pay(Store store, float payment, Date transactionDate) {
        this.financialManager.withdraw(payment, transactionDate);
        store.getPayment(payment, transactionDate);
    }

    public List<Integer> getFeedbackableStores() {
         return this.getCurrentOrder().getStoresOfOrderIds();
    }

    public SDMOrdersHistory getZoneOrdersHistory(String zoneName) {
        return this.zonesOrderHistory.get(zoneName);
    }
}