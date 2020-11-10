package StoresDetailsConteiner;

import EngineClasses.Location.Location;
import EngineClasses.Store.Discount.StoreDiscounts;
import ItemsDetailsContainer.ItemDetailsContainer;
import OrderConteiner.OrderContainer;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsContainer {
    private int id;
    private String name;
    private String ownerName;
    private Location location;
    private List<ItemDetailsContainer> itemsDetails = new ArrayList<>();
    private List<OrderContainer> ordersHistory = new ArrayList<>();
    private int ppk;
    private float totalDeliveriesIncome;
    private float distanceFromCustomer;
    private float deliveryPrice;
    private int numberOfDifferentItems;
    private int numberOfOrders;
    private float itemsIncome;
    private float deliveriesIncome;
    private StoreDiscounts storeDiscounts = new StoreDiscounts();

    public float getDeliveriesIncome() {
        return this.deliveriesIncome;
    }

    public void setDeliveriesIncome(float deliveriesIncome) {
        this.deliveriesIncome = deliveriesIncome;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public int getNumberOfOrders() {
        return this.numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public StoreDiscounts getStoreDiscounts() {
        return this.storeDiscounts;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setStoreDiscounts(StoreDiscounts storeDiscounts) {
        this.storeDiscounts = storeDiscounts;
    }

    public float getDistanceFromCustomer() {
        return distanceFromCustomer;
    }

    public void setDistanceFromCustomer(float distanceFromCustomer) {
        this.distanceFromCustomer = distanceFromCustomer;
    }

    public float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(float deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getNumberOfDifferentItems() {
        return numberOfDifferentItems;
    }

    public void setNumberOfDifferentItems(int numberOfDifferentItems) {
        this.numberOfDifferentItems = numberOfDifferentItems;
    }

    public float getItemsIncome() {
        return itemsIncome;
    }

    public void setItemsIncome(float itemsPrice) {
        this.itemsIncome = itemsPrice;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemDetailsContainer> getItemsDetails() {
        return this.itemsDetails;
    }

    public void setItemsDetails(List<ItemDetailsContainer> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

    public List<OrderContainer> getOrdersHistory() {
        return this.ordersHistory;
    }

    public void setOrdersHistory(List<OrderContainer> ordersHistory) {
        this.ordersHistory = ordersHistory;
    }

    public int getPpk() {
        return this.ppk;
    }

    public void setPpk(int ppk) {
        this.ppk = ppk;
    }

    public float getTotalDeliveriesIncome() {
        return this.totalDeliveriesIncome;
    }

    public void setTotalDeliveriesIncome(float totalDeliveriesIncome) {
        this.totalDeliveriesIncome = totalDeliveriesIncome;
    }

    public void addItemDetailsContainer(ItemDetailsContainer itemDetailsContainer) {
        itemsDetails.add(itemDetailsContainer);
    }

    public void addOrderContainer(OrderContainer orderContainer) {
        ordersHistory.add(orderContainer);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isItemExist(int itemId) {
        for (ItemDetailsContainer itemDetailsContainer : this.itemsDetails) {
            if (itemDetailsContainer.getId() == itemId) {
                return true;
            }
        }

        return false;
    }

    public ItemDetailsContainer getItem(int itemId) {
        for (ItemDetailsContainer itemDetailsContainer : this.itemsDetails) {
            if (itemDetailsContainer.getId() == itemId) {
                return itemDetailsContainer;
            }
        }

        return null;
    }
}