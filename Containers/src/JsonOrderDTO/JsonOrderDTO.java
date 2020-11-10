package JsonOrderDTO;

import EngineClasses.Location.Location;
import EngineClasses.Order.Order;
import OrderConteiner.ItemsPerStoreContainer;
import OrderConteiner.OrderContainer;

import java.util.*;

public class JsonOrderDTO {
    private int orderId;
    private Date arrivalDate;
    private Location arrivalLocation;
    private int numberOfDifferentItems;
    private float totalOrderPrice;
    private float itemsPrice;
    private int totalUnits;
    private float deliveryPrice;
    private String customerUsername;

    private List<Integer> boughtItemsStoresIds = new ArrayList<>();
    private List<String> boughtItemsStoresNames = new ArrayList<>();
    private List<ItemsPerStoreContainer> boughtItems = new ArrayList<>();

    private List<Integer> dealItemsStoreIds = new ArrayList<>();
    private List<ItemsPerStoreContainer> deals = new ArrayList<>();

    public JsonOrderDTO(Order order) {
        orderId = order.getId();
        arrivalDate = order.getArrivalDate();
        arrivalLocation = order.getArrivalLocation();
        numberOfDifferentItems = order.getOrderItems().size();
        totalOrderPrice = order.getTotalPrice();
        itemsPrice = order.getItemsPrice();
        totalUnits = order.getNumberOfUnits();
        deliveryPrice = order.getDeliveryPrice();
        customerUsername = order.getCustomer().getName();

        boughtItemsStoresIds.addAll(order.getStoresOfOrderIds());
        boughtItemsStoresNames.addAll(order.getStoreNamesList());
        boughtItems.addAll(order.getBoughtItemDetailsContainerList());

        dealItemsStoreIds.addAll(order.getDealItemsStoreIds());
        deals.addAll(order.getDealItemDetailsContainerList());
    }

    public JsonOrderDTO(){}

    public void setBoughtItemsStoresNames(List<String> boughtItemsStoresNames) {
        this.boughtItemsStoresNames = boughtItemsStoresNames;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    public int getNumberOfDifferentItems() {
        return numberOfDifferentItems;
    }

    public float getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public float getItemsPrice() {
        return itemsPrice;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public float getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<Integer> getBoughtItemsStoresIds() {
        return boughtItemsStoresIds;
    }

    public List<String> getBoughtItemsStoresNames() {
        return boughtItemsStoresNames;
    }

    public List<ItemsPerStoreContainer> getBoughtItems() {
        return boughtItems;
    }

    public List<Integer> getDealItemsStoreIds() {
        return dealItemsStoreIds;
    }

    public List<ItemsPerStoreContainer> getDeals() {
        return deals;
    }

    public void fillJsonOrderDTO(OrderContainer orderContainer){
        orderId = orderContainer.getOrderId();
        arrivalDate = orderContainer.getArrivalDate();
        arrivalLocation = orderContainer.getArrivalLocation();
        numberOfDifferentItems = orderContainer.getNumberOfDifferentItems();
        totalOrderPrice = orderContainer.getTotalOrderPrice();
        itemsPrice = orderContainer.getItemsPrice();
        totalUnits = orderContainer.getTotalUnits();
        deliveryPrice = orderContainer.getDeliveryPrice();
        customerUsername = orderContainer.getCustomer().getName();

        boughtItemsStoresIds.addAll(orderContainer.getStoreIdToSeller().keySet());
        boughtItems.addAll(orderContainer.getStoreIdToSeller().values());

        dealItemsStoreIds.addAll(orderContainer.getStoreIdToDeals().keySet());
        deals.addAll(orderContainer.getStoreIdToDeals().values());
    }
}
