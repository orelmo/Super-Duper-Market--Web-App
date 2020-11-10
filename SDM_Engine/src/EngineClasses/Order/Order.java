package EngineClasses.Order;

import EngineClasses.Location.Location;
import OrderConteiner.ItemsPerStoreContainer;
import OrderConteiner.OrderContainer;
import ItemsDetailsContainer.*;
import SDMSystem.SDMSystemCustomer;
import SDMSystem.SDMZone;
import SDMSystem.SDMZoneItems;

import java.time.LocalDate;
import java.util.*;

public class Order {
    private static int idFactory = 1;
    private int id;
    private Date arrivalDate;
    private Location arrivalLocation;
    private float itemsPrice;
    private float totalPrice;
    private float deliveryPrice;
    private int numberOfUnits;
    private StoresOfOrder storesOfOrder;
    private SDMSystemCustomer customer;
    private SDMZone zoneBoughtFrom;

    public int getNumberOfUnits() {
        return this.numberOfUnits;
    }

    public float getTotalPrice() {
        return this.totalPrice;
    }

    public float getItemsPrice() {
        return this.itemsPrice;
    }

    public SDMSystemCustomer getCustomer() {
        return customer;
    }

    public Order(OrderContainer orderContainer) {
        this.id = idFactory++;
        this.customer = orderContainer.getCustomer();
        this.storesOfOrder = new StoresOfOrder();
        this.storesOfOrder.extractStores(orderContainer);
        this.arrivalDate = orderContainer.getArrivalDate();
        this.arrivalLocation = orderContainer.getArrivalLocation();
        if (orderContainer.getItemsPrice() == 0) {
            this.itemsPrice = calcItemsPrice(orderContainer);
        } else {
            this.itemsPrice = orderContainer.getItemsPrice();
        }
        this.deliveryPrice = calcDeliveryPrice(orderContainer.getStoreIdToSeller());
        this.numberOfUnits = orderContainer.getTotalUnits();
        this.totalPrice = this.deliveryPrice + this.itemsPrice;
    }

    private Order(){}

    private float calcDeliveryPrice(Map<Integer, ItemsPerStoreContainer> storeIdToSeller) {
        float deliveryPrice = 0;
        for(ItemsPerStoreContainer itemsPerStoreContainer : storeIdToSeller.values()){
            deliveryPrice += itemsPerStoreContainer.getDeliveryPrice();
        }

        return deliveryPrice;
    }

    public Location getArrivalLocation() {
        return this.arrivalLocation;
    }

    public StoresOfOrder getStoresOfOrder() {
        return this.storesOfOrder;
    }

    public static void setIdFactory(int newIdFactory) {
        idFactory = newIdFactory;
    }

    private float calcItemsPrice(OrderContainer orderContainer) {
        float price = 0;
        for (ItemsPerStoreContainer itemsPerStoreContainer : orderContainer.getStoreIdToSeller().values()) {
            for(ItemDetailsContainer itemDetailsContainer : itemsPerStoreContainer.getItems()){
                price += itemDetailsContainer.getPriceAtStore();
            }
        }
        for (ItemsPerStoreContainer itemsPerStoreContainer : orderContainer.getStoreIdToDeals().values()) {
            for(ItemDetailsContainer itemDetailsContainer : itemsPerStoreContainer.getItems()){
                price += itemDetailsContainer.getPriceAtStore();
            }
        }
            return price;
    }

    public float getDeliveryPricesFromStore(int storeId) {
        return this.storesOfOrder.getDeliveryPriceFromStore(storeId);
    }

    public float getAmountForItem(int itemId) {
        return this.storesOfOrder.getAmountForItem(itemId);
    }

    public Date getArrivalDate() {
        return this.arrivalDate;
    }

    public int getId() {
        return this.id;
    }

    public int getUnitsNumber(int storeId, SDMZoneItems systemItems) {
        return this.storesOfOrder.getUnitsNumberOfStore(storeId, systemItems);
    }

    public Collection<Integer> getOrderItems() {
        List<Integer> itemsIds = new ArrayList<>();
        for(StoreOfOrder storeOfOrder : this.storesOfOrder.getStoresOfOrder()){
            for(Integer itemId : storeOfOrder.getItemsIds()){
                itemsIds.add(itemId);
            }
        }

        return itemsIds;
    }

    public Collection<Integer> getStoresOfOrderIds() {
       return this.storesOfOrder.getStoresIds();
    }

    public Order getSubOrderPerStore(Integer storeId, SDMZoneItems zoneItems) {
        Order subOrder = new Order();
        subOrder.id = this.id;
        subOrder.arrivalLocation = this.arrivalLocation;
        subOrder.arrivalDate = this.arrivalDate;
        subOrder.customer = this.customer;
        subOrder.storesOfOrder = new StoresOfOrder();
        subOrder.storesOfOrder.addStoreOfOrder(storeId, this.storesOfOrder.getStoreOfOrder(storeId));
        subOrder.deliveryPrice = this.storesOfOrder.getDeliveryPriceFromStore(storeId);
        subOrder.itemsPrice = this.storesOfOrder.getItemsPriceFromStore(storeId);
        subOrder.numberOfUnits = this.storesOfOrder.getStoreOfOrder(storeId).getNumberOfUnits(zoneItems);
        subOrder.totalPrice = subOrder.deliveryPrice + subOrder.itemsPrice;
        subOrder.setZoneBoughtFrom(this.zoneBoughtFrom);

        return subOrder;
    }

    public float getDeliveryPrice() {
        return this.deliveryPrice;
    }

    public float getDeliveryPricePerStore(Integer storeId) {
        return this.storesOfOrder.getDeliveryPriceFromStore(storeId);
    }

    public AmountForItem getItemsPerStore(Integer storeId) {
        return this.storesOfOrder.getStoreOfOrder(storeId).getAmountForItem();
    }

    public StoreOfOrder getStoreOfOrder(Integer storeId) {
        return this.storesOfOrder.getStoreOfOrder(storeId);
    }

    public List<ItemsPerStoreContainer> getBoughtItemDetailsContainerList() {
        List<ItemsPerStoreContainer> itemsPerStoreContainerList = new ArrayList<>();
        for (Integer storeId : this.storesOfOrder.getStoresIds()) {
            StoreOfOrder storeOfOrder = this.storesOfOrder.getStoreOfOrder(storeId);
            ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
            for (Integer itemId : storeOfOrder.getAmountForItem().getItemsId()) {
                ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
                itemDetailsContainer.setId(itemId);
                itemDetailsContainer.setName(zoneBoughtFrom.getItem(itemId).getName());
                itemDetailsContainer.setPurchaseCategory(zoneBoughtFrom.getItem(itemId).getPurchaseCategory());
                itemDetailsContainer.setStoresSellingTheItem(zoneBoughtFrom.getNumberOfStoresSellingTheItem(itemId));
                itemDetailsContainer.setAvgPrice(zoneBoughtFrom.getAvgItemPrice(itemId));
                itemDetailsContainer.setSoldCounter(zoneBoughtFrom.getItemSoldCounter(itemId));
                itemDetailsContainer.setPriceAtStore(zoneBoughtFrom.getStore(storeId).getItemPrice(itemId));
                itemDetailsContainer.setAmount(storeOfOrder.getAmount(itemId));
                itemsPerStoreContainer.addItem(itemDetailsContainer);
            }
            itemsPerStoreContainerList.add(itemsPerStoreContainer);
        }

        return itemsPerStoreContainerList;
    }

    public void setZoneBoughtFrom(SDMZone sdmZone) {
        this.zoneBoughtFrom = sdmZone;
    }

    public List<String> getStoreNamesList() {
        List<String> storesNames = new ArrayList<>();
        for(Integer storeId : this.getStoresOfOrder().getStoresIds()){
            storesNames.add(zoneBoughtFrom.getStore(storeId).getName());
        }

        return storesNames;
    }

    public List<Integer> getDealItemsStoreIds() {
        List<Integer> dealsItemsStoreIds = new ArrayList<>();
        for(Integer storeId : this.storesOfOrder.getStoresIds()){
            if(this.storesOfOrder.getStoreOfOrder(storeId).getDealItems().size() > 0){
                dealsItemsStoreIds.add(storeId);
            }
        }

        return dealsItemsStoreIds;
    }

    public List<ItemsPerStoreContainer> getDealItemDetailsContainerList() {
        List<ItemsPerStoreContainer> dealsItems = new ArrayList<>();
        for(Integer storeId : this.storesOfOrder.getStoresIds()){
            if(this.storesOfOrder.getStoreOfOrder(storeId).getDealItems().size() > 0){
                ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
                for(DealItem deal : this.storesOfOrder.getStoreOfOrder(storeId).getDealItems()){
                    ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
                    itemDetailsContainer.setId(deal.getId());
                    itemDetailsContainer.setName(deal.getName());
                    itemDetailsContainer.setPurchaseCategory(deal.getPurchaseCategory());
                    itemDetailsContainer.setPriceAtStore(deal.getPrice());
                    itemDetailsContainer.setAmount(deal.getAmount());
                    itemDetailsContainer.setSoldCounter(this.zoneBoughtFrom.getItemSoldCounter(deal.getId()));
                    itemDetailsContainer.setAvgPrice(this.zoneBoughtFrom.getAvgItemPrice(deal.getId()));
                    itemDetailsContainer.setStoresSellingTheItem(this.zoneBoughtFrom.getNumberOfStoresSellingTheItem(deal.getId()));
                    itemsPerStoreContainer.addItem(itemDetailsContainer);
                }
                dealsItems.add(itemsPerStoreContainer);
            }
        }
        return dealsItems;
    }
}