package SDMSystem;

import EngineClasses.Interfaces.Locationable;
import EngineClasses.Item.Item;
import EngineClasses.Item.ePurchaseCategory;
import EngineClasses.Location.Location;
import EngineClasses.Order.DealItem;
import EngineClasses.Order.Order;
import EngineClasses.Store.Discount.Discount;
import EngineClasses.Store.Sell;
import EngineClasses.Store.Store;
import EngineClasses.Users.Notifications.NewStoreNotification;
import Exceptions.*;
import ItemsDetailsContainer.ItemsDetailsContainer;
import ItemsDetailsContainer.ItemDetailsContainer;
import OrderConteiner.ItemsPerStoreContainer;
import OrderConteiner.OrderContainer;
import StoresDetailsConteiner.StoreDetailsContainer;
import StoresDetailsConteiner.StoresDetailsContainer;
import com.google.gson.internal.LinkedTreeMap;
import jaxbClasses.*;

import javax.naming.directory.NoSuchAttributeException;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SDMZone {
    private static final int unSold = -1;

    private String name;
    private SDMZoneItems zoneItems;
    private SDMZoneStores zoneStores;
    private SDMZoneLocations zoneLocations;
    private SDMSystemSeller owner;
    private int numberOfDifferentItems;
    private int numberOfStores;
    private int numberOfOrders;
    private float ordersItemsAvgPrice;
    private SDMSystem sdmSystem;

    public SDMZone() {
        this.zoneItems = new SDMZoneItems();
        this.zoneStores = new SDMZoneStores();
        this.zoneLocations = new SDMZoneLocations();
        this.owner = new SDMSystemSeller();
        this.sdmSystem = new SDMSystem();
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setNumberOfDifferentItems(int numberOfDifferentItems) {
        this.numberOfDifferentItems = numberOfDifferentItems;
    }

    public SDMSystemSeller getOwner() {
        return this.owner;
    }

    public int getNumberOfDifferentItems() {
        return this.numberOfDifferentItems;
    }

    public void analyzeZoneDetails(){
        this.numberOfDifferentItems = this.zoneItems.getNumberOfDifferentItems();
        this.numberOfStores = this.zoneStores.getNumberOfStores();
    }

    public int getNumberOfStores() {
        return this.numberOfStores;
    }

    public int getNumberOfOrders() {
        return this.numberOfOrders;
    }

    public float getAvgOrderItemsPrice() {
        return this.ordersItemsAvgPrice;
    }

    public void setOwner(SDMSystemSeller owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateNumberOfStores(int amount){
        this.numberOfStores += amount;
    }

    public void updateNumberOfOrders(int amount){
        this.numberOfOrders += amount;
    }

    public void checkIfAllItemsHaveSeller() {
        for (SDMZoneItem item : this.zoneItems.getIterable()) {
            if (item.getNumberOfStoresSellingTheItem() == 0) {
                throw new NoSellerException("Item with id " + item.getId() + " have no seller");
            }
        }
    }

    public void extractSDMStores(SDMStores sdmStores) throws NoSuchAttributeException {
        for (SDMStore store : sdmStores.getSDMStore()) {
            if (this.zoneStores.isExist(store.getId())) {
                throw new IdAmbiguityException("Store with id " + store.getId() + " already taken");
            }

            if (isTakenLocation(store.getLocation())) {
                throw new LocationException("The store with id " + store.getId() + " Location is already taken");
            }

            Store newStore = new Store(store, this);
            this.owner.addStore(this.name, newStore);
            this.zoneStores.add(store.getId(), newStore);
            this.informLocalized(newStore.getLocation(), newStore);
        }
    }

    private boolean isTakenLocation(jaxbClasses.Location location) {
        return this.zoneLocations.isExist(new Location(location));
    }
    private boolean isTakenLocation(Location location) {
        return this.zoneLocations.isExist(location);
    }

    public void extractSDMItems(SDMItems sdmItems) {
        for (SDMItem item : sdmItems.getSDMItem()) {
            if (this.zoneItems.isExist(item.getId())) {
                throw new IdAmbiguityException("Item with id " + item.getId() + " already taken");
            }

            SDMZoneItem newItem = new SDMZoneItem(new Item(item));
            this.zoneItems.add(item.getId(), newItem);
        }
    }

    public ItemsDetailsContainer getAllItemsDetails() {
        ItemsDetailsContainer itemsDetailsContainer = new ItemsDetailsContainer();
        for (SDMZoneItem item : this.zoneItems.getIterable()) {
            ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
            itemDetailsContainer.setId(item.getId());
            itemDetailsContainer.setName(item.getName());
            itemDetailsContainer.setPurchaseCategory(item.getPurchaseCategory());
            itemDetailsContainer.setStoresSellingTheItem(item.getNumberOfStoresSellingTheItem());
            itemDetailsContainer.setAvgPrice(item.getAvgPrice());
            itemDetailsContainer.setSoldCounter(item.getSoldCounter());
            itemsDetailsContainer.add(itemDetailsContainer);
        }

        return itemsDetailsContainer;
    }

    public StoresDetailsContainer getStoresDetailsForOrder() {
        StoresDetailsContainer storesDetails = new StoresDetailsContainer();
        for (Store store : this.zoneStores.getIterable()) {
            StoreDetailsContainer storeDetails = new StoreDetailsContainer();
            storeDetails.setId(store.getId());
            storeDetails.setName(store.getName());
            storeDetails.setPpk(store.getPPK());
            storesDetails.add(storeDetails);
        }

        return storesDetails;
    }

    public boolean isStoreExist(String input) throws NumberFormatException {
        return this.zoneStores.isExist(Integer.parseInt(input));
    }

    public Date parseOrderArrivalDate(String inputDate) {
        DateFormat formatter = new SimpleDateFormat("dd/MM-HH:mm");
        formatter.setLenient(false);
        Date date = null;
        try {
            date = formatter.parse(inputDate);
        } catch (ParseException e) {
            return null;
        }

        return date;
    }

    public Location parseLocation(String xInput, String yInput) {
        int x, y;
        try {
            x = Integer.parseInt(xInput);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("x coordinate is not an integer");
        }
        try {
            y = Integer.parseInt(yInput);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("y coordinate is not an integer");
        }

        return new Location(x, y);
    }

    public boolean isValidOrderLocation(Location location) {
        for (Store store : this.zoneStores.getIterable()) {
            if (store.getLocation().equals(location)) {
                return false;
            }
        }

        return true;
    }

    public ItemsDetailsContainer getAllItemsDetailsForOrder(int selectedStoreId) {
        ItemsDetailsContainer itemsDetails = new ItemsDetailsContainer();
        for (Item item : this.zoneItems.getIterable()) {
            ItemDetailsContainer itemDetails = new ItemDetailsContainer();
            itemDetails.setId(item.getId());
            itemDetails.setName(item.getName());
            itemDetails.setPurchaseCategory(item.getPurchaseCategory());
            if (this.zoneStores.isStoreSellingItem(selectedStoreId, item.getId()) == false) {
                itemDetails.setPriceAtStore(unSold);
            } else {
                itemDetails.setPriceAtStore(this.zoneStores.getItemPriceFromStore(selectedStoreId, item.getId()));
            }
            itemsDetails.add(itemDetails);
        }

        return itemsDetails;
    }

    public boolean isFinishQ(String input) {
        return input.toLowerCase().equals("q");
    }

    public int parseStringToId(String input) throws NumberFormatException, UnexistItemException {
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The id you entered is not an integer");
        }
        if (this.zoneItems.get(id) == null) {
            throw new UnexistItemException("No item with id: " + id);
        }

        return id;
    }

    public boolean isItemForSale(int itemId) {
        return this.zoneItems.get(itemId) != null;
    }

    public boolean isStoreSellingItem(int selectedStoreId, int itemId) {
        return this.zoneStores.isStoreSellingItem(selectedStoreId, itemId);
    }

    public Number parseStringToAmount(String amountInput) {
        Number amount;
        try {
            amount = Integer.parseInt(amountInput);
        } catch (NumberFormatException e) {
            try {
                amount = Float.parseFloat(amountInput);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("The amount is not an integer or float");
            }
        }

        return amount;
    }

    public boolean isValidAmountForItem(Number amount, int itemId) {
        return (this.zoneItems.get(itemId).getPurchaseCategory().equals(ePurchaseCategory.Weight)
                && (amount instanceof Float || amount instanceof Integer)) ||
                (this.zoneItems.get(itemId).getPurchaseCategory().equals(ePurchaseCategory.Quantity)
                        && amount instanceof Integer);
    }

    public boolean isItemExist(int itemId) {
        return this.zoneItems.isExist(itemId);
    }

    public Item getItem(int itemId) {
        return this.zoneItems.get(itemId);
    }

    public int getItemPriceFromStore(int storeId, int itemId) {
        return this.zoneStores.getItemPriceFromStore(storeId, itemId);
    }

    public void updateSDMItemDetails(int itemId, int newPrice) {
        this.zoneItems.updateSDMItemDetails(itemId, newPrice);
    }

    public float calcDistanceFromStore(Store store, Location arrivalLocation) {
        return (float) Math.sqrt(Math.pow((store.getLocation().getY() - arrivalLocation.getY()), 2) +
                Math.pow((store.getLocation().getX() - arrivalLocation.getX()), 2));
    }

    public float getDeliveryPrice(Store store, Location location){
        return calcDistanceFromStore(store, location) * store.getPPK();
    }

    private float calcItemsPrice(int price, Number amount) {
        if (amount instanceof Float) {
            return amount.floatValue() * price;
        } else if (amount instanceof Integer) {
            return amount.intValue() * price;
        } else {
            throw new NumberFormatException("Amount data type is not supported");
        }
    }

    public void executeOrder(OrderContainer orderContainer) {
        Order order = new Order(orderContainer);
        order.setZoneBoughtFrom(this);
        this.ordersItemsAvgPrice = calcUpdatedAVG(this.ordersItemsAvgPrice,this.numberOfOrders,order.getItemsPrice());
        this.numberOfOrders += 1;
        updateStoresAfterOrder(order);
        updateSystemItemsAfterOrder(order);
        updateCustomerAfterOrder(order);
        makeOrderPayment(order);
        notifyNewOrder(order);
    }

    private void notifyNewOrder(Order order) {
        for(Integer storeId : order.getStoresOfOrderIds()){
            this.zoneStores.get(storeId).notifyNewOrder(order.getSubOrderPerStore(storeId, this.zoneItems));
        }
    }

    private void makeOrderPayment(Order order) {
        SDMSystemCustomer customer = order.getCustomer();
        for(Integer storeId : order.getStoresOfOrderIds()){

            customer.pay(this.getStore(storeId), order.getStoreOfOrder(storeId).getItemsPrice()
                    + order.getStoreOfOrder(storeId).getDeliveryPrice(), order.getArrivalDate());
        }
    }

    private void updateCustomerAfterOrder(Order order) {
        SDMSystemCustomer customer = order.getCustomer();
        customer.addOrderToOrderHistory(this.name,order);
        customer.setAvgOrdersDeliveryPrice(this.calcUpdatedAVG(customer.getAvgOrdersDeliveryPrice(), customer.getNumberOfOrders(), order.getDeliveryPrice()));
        customer.setAvgOrdersItemsPrice(this.calcUpdatedAVG(customer.getAvgOrdersItemsPrice(), customer.getNumberOfOrders(), order.getItemsPrice()));
        customer.setNumberOfOrders(customer.getNumberOfOrders() + 1);
    }

    private float calcUpdatedAVG(float prevAVG, int prevNumberOfObject, float newValue) {
        return (prevAVG * prevNumberOfObject + newValue) / (prevNumberOfObject + 1);
    }

    private void updateSystemItemsAfterOrder(Order order) {

        for (Integer storeId : order.getStoresOfOrderIds()) {
            for (Integer itemId : order.getStoreOfOrder(storeId).getAmountForItem().getItemsId()) {
                this.zoneItems.get(itemId).updateSoldCounter(order.getStoreOfOrder(storeId).getAmountForItem().get(itemId).floatValue());
            }
            for (DealItem dealItem : order.getStoreOfOrder(storeId).getDealItems()) {
                this.zoneItems.get(dealItem.getId()).updateSoldCounter(dealItem.getAmount());
            }
        }
    }

    private void updateStoresAfterOrder(Order order) {
        for (Integer storeId : order.getStoresOfOrderIds()) {
            this.zoneStores.get(storeId).addOrder(order.getSubOrderPerStore(storeId, this.zoneItems));
            this.zoneStores.get(storeId).updatePaymentForDelivery(order.getDeliveryPricePerStore(storeId));
            this.zoneStores.get(storeId).increaseNumberOfSoldUnits(order.getUnitsNumber(storeId, this.zoneItems));
            this.zoneStores.get(storeId).increaseStoreItemsSoldCounter(order.getStoreOfOrder(storeId));
        }
    }

    public void fillStoresDetailsContainer(StoresDetailsContainer storesDetailsContainer) {
        for (Store systemStore : this.zoneStores.getIterable()) {
            StoreDetailsContainer storeDetailsContainer = new StoreDetailsContainer();
            storeDetailsContainer.setId(systemStore.getId());
            storeDetailsContainer.setName(systemStore.getName());
            storeDetailsContainer.setOwnerName(systemStore.getOwner().getName());
            storeDetailsContainer.setItemsIncome(systemStore.analyzeItemsIncome());
            storeDetailsContainer.setDeliveriesIncome(systemStore.getTotalDeliveriesPayment());
            storeDetailsContainer.setLocation(systemStore.getLocation());

            for (Sell storeItem : systemStore.getStoreItems().getIterable()) {
                ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
                itemDetailsContainer.setId(storeItem.getItemId());
                itemDetailsContainer.setName(this.getItem(storeItem.getItemId()).getName());
                itemDetailsContainer.setPurchaseCategory(this.getItem(storeItem.getItemId()).getPurchaseCategory());
                itemDetailsContainer.setPriceAtStore(storeItem.getPrice());
                itemDetailsContainer.setSoldCounter(storeItem.getSold());
                storeDetailsContainer.addItemDetailsContainer(itemDetailsContainer);
            }

            for (Order order : systemStore.getOrdersHistory().getIterable()) {
                OrderContainer orderContainer = new OrderContainer();
                orderContainer.setOrderId(order.getId());
                orderContainer.setArrivalDate(order.getArrivalDate());
                orderContainer.setItemsPrice(order.getItemsPrice());
                orderContainer.setTotalOrderPrice(order.getTotalPrice());
                orderContainer.setTotalUnits(order.getNumberOfUnits());
                orderContainer.setDeliveryPrice(order.getDeliveryPrice());
                fillOrderContainerItemPerStore(orderContainer, order, systemStore);

                storeDetailsContainer.addOrderContainer(orderContainer);
            }

            storeDetailsContainer.setPpk(systemStore.getPPK());
            storeDetailsContainer.setTotalDeliveriesIncome(systemStore.getTotalDeliveriesPayment());
            storeDetailsContainer.setStoreDiscounts(systemStore.getStoreDiscounts());
            storesDetailsContainer.add(storeDetailsContainer);

        }
    }

    private void fillOrderContainerItemPerStore(OrderContainer orderContainer, Order order, Store systemStore) {
        ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
        for (Integer itemId : order.getItemsPerStore(systemStore.getId()).getItemsId()) {
            ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
            itemDetailsContainer.setId(itemId);
            itemDetailsContainer.setName(this.getItem(itemId).getName());
            itemDetailsContainer.setPurchaseCategory(this.getItem(itemId).getPurchaseCategory());
            itemDetailsContainer.setPriceAtStore(systemStore.getItemPrice(itemId));
            itemDetailsContainer.setSoldCounter(systemStore.getStoreItems().get(itemId).getSold());

            itemsPerStoreContainer.getItems().add(itemDetailsContainer);
        }

        orderContainer.addItemsPerStoreContainer(systemStore.getId(), itemsPerStoreContainer);
    }

    public boolean isItemSoldByStore(int selectedStoreId, int id) {
        return this.zoneStores.get(selectedStoreId).isSellingItem(id);
    }

    public boolean isValidMenuOptionSelection(String input) {
        int inputAsInt;
        try {
            inputAsInt = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return (inputAsInt >= 1 && inputAsInt <= eMenuOptions.values().length);
    }

    public eMenuOptions parseStringToEMenuOption(String input) {
        int inputInt = Integer.parseInt(input);
        for (eMenuOptions option : eMenuOptions.values()) {
            if (option.getId() == inputInt) {
                return option;
            }
        }

        return null;
    }

    public boolean isEmptyOrder(OrderContainer orderContainer) {
        return orderContainer.getNumberOfDifferentItems() == 0;
    }

    public void exitSystem() {
    }

    public void deleteItemFromStore(int storeId, int itemId) {
        if (this.isStoreSellingItem(storeId, itemId) == false) {
            throw new UnexistItemException("This store doesn't sell the item you selected");
        } else if (isItemSoldByMoreThanOneStore(itemId) == false) {
            throw new CannotBeDeletedException("The item is being sold only by this store and therefore can't be deleted");
        } else {
            updateAvgItemPriceAfterDelete(itemId, getItemPriceFromStore(storeId, itemId));
            this.zoneStores.get(storeId).deleteItem(itemId);
            this.zoneItems.get(itemId).decreaseNumberOfStoresSellingTheItem();
        }
    }

    private void updateAvgItemPriceAfterDelete(int itemId, int itemPriceFromStore) {
        this.zoneItems.get(itemId).setAvgPrice(
                ((this.zoneItems.get(itemId).getAvgPrice()) * (this.zoneItems.get(itemId).getNumberOfStoresSellingTheItem())
                        - itemPriceFromStore) / (this.zoneItems.get(itemId).getNumberOfStoresSellingTheItem() - 1));
    }

    private boolean isItemSoldByMoreThanOneStore(int itemId) {
        return this.zoneItems.get(itemId).getNumberOfStoresSellingTheItem() > 1;
    }

    public int parseStringToPrice(String priceString) {
        int price;
        try {
            price = Integer.parseInt(priceString);
            if (price < 1) {
                throw new IllegalPriceException("Item price cannot be below 1");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Item price must be integer");
        }

        return price;
    }

    public void addItemToStore(int storeId, int itemId, int itemPrice) {
        this.zoneStores.get(storeId).addItem(new Sell(itemId, itemPrice));
        this.zoneItems.get(itemId).updateAvgPriceAfterAdding(itemPrice);
        this.zoneItems.get(itemId).increaseSellersCounter();
    }

    public void updateItemPriceInStore(int storeId, int itemId, int itemNewPrice) {
        if (this.isStoreSellingItem(storeId, itemId) == false) {
            throw new UnexistItemException("The store doesn't sell the item you selected");
        }
        this.updateItemAvgPrice(storeId, itemId, itemNewPrice);
        this.zoneStores.get(storeId).updateItemPrice(itemId, itemNewPrice);
    }

    private void updateItemAvgPrice(int storeId, int itemId, int itemNewPrice) {
        this.zoneItems.get(itemId).setAvgPrice((this.zoneItems.get(itemId).getAvgPrice() * this.zoneItems.get(itemId).getNumberOfStoresSellingTheItem()
                - this.getItemPriceFromStore(storeId, itemId) + itemNewPrice) / this.zoneItems.get(itemId).getNumberOfStoresSellingTheItem());
    }

    private Map<Integer, Number> getItemToAmountFromFile(Scanner scanner, int numberOfItems) {
        Map<Integer, Number> itemToAmount = new HashMap<>();
        Number amount;
        String amountString;
        for (int i = 0; i < numberOfItems; ++i) {
            Integer itemId = Integer.parseInt(scanner.nextLine());
            amountString = scanner.nextLine();
            try {
                amount = Integer.parseInt(amountString);
            } catch (Exception e) {
                amount = Float.parseFloat(amountString);
            }
            itemToAmount.put(itemId, amount);
        }

        return itemToAmount;
    }

    private void informLocalized(Location location, Locationable locationable) {
        this.zoneLocations.add(location, locationable);
    }

//    public void showAllItems(ItemsAdder uiAdapter) {
//        ShowItemsTask showItemsTask = new ShowItemsTask(uiAdapter, getAllItemsDetails());
//        new Thread(showItemsTask).start();
//    }
//
//    public void showAllStores(SDMShowStoresPage.UIAdapter uiAdapter) {
//        StoresDetailsContainer storesDetailsContainer = new StoresDetailsContainer();
//        fillStoresDetailsContainer(storesDetailsContainer);
//        ShowStoresTask showStoresTask = new ShowStoresTask(uiAdapter, storesDetailsContainer);
//        new Thread(showStoresTask).start();
//    }
//
//    public void showAllCustomers(SDMShowCustomersPage.UIAdapter uiAdapter) {
//        ShowCustomersTask showCustomersTask = new ShowCustomersTask(uiAdapter, this.systemCustomers);
//        new Thread(showCustomersTask).start();
//    }
//
//    public void fillStoresNamesToUiAdapter(StoresAdder uiAdapter) {
//        StoresDetailsContainer storesDetailsContainer = new StoresDetailsContainer();
//        this.fillStoresDetailsContainer(storesDetailsContainer);
//        FillStoresNamesTask fillStoresNamesTask = new FillStoresNamesTask(uiAdapter, storesDetailsContainer);
//        new Thread(fillStoresNamesTask).start();
//    }
//
//    public void fillAddableItemsPerStore(int storeId, SDMAddItemToStorePage.UIAdapter uiAdapter) {
//        FillItemsNamesTask fillStoresNamesTask = new FillItemsNamesTask(uiAdapter, this.getItemsNotInStore(storeId));
//        new Thread(fillStoresNamesTask).start();
//    }

    private SDMZoneItems getItemsNotInStore(int storeId) {
        SDMZoneItems itemsNotInStore = new SDMZoneItems();
        for (SDMZoneItem item : this.zoneItems.getIterable()) {
            if (this.isStoreSellingItem(storeId, item.getId()) == false) {
                itemsNotInStore.add(item.getId(), item);
            }
        }

        return itemsNotInStore;
    }

//    public void fillDeletableItemsPerStore(int selectedStoreId, SDMDeleteItemFromStorePage.UIAdapter uiAdapter) {
//        if (this.systemStores.get(selectedStoreId) != null &&
//                this.systemStores.get(selectedStoreId).getStoreItems().getIterable() != null) {
//            for (Sell storeSellItem : this.systemStores.get(selectedStoreId).getStoreItems().getIterable()) {
//                if (isItemSoldByMoreThanOneStore(storeSellItem.getItemId())) {
//                    uiAdapter.addItemToShow(new ItemDetailsContainer(this.systemItems.get(storeSellItem.getItemId())));
//                }
//            }
//        }
//    }
//
//    public void fillAllItemsPerStore(int selectedStoreId, ItemsAdder uiAdapter) {
//        if (this.systemStores.get(selectedStoreId) != null &&
//                this.systemStores.get(selectedStoreId).getStoreItems().getIterable() != null) {
//            for (Sell storeSellItem : this.systemStores.get(selectedStoreId).getStoreItems().getIterable()) {
//                ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer(this.systemItems.get(storeSellItem.getItemId()));
//                itemDetailsContainer.setPriceAtStore(storeSellItem.getPrice());
//                uiAdapter.addItemToShow(itemDetailsContainer);
//            }
//        }
//    }
//
//    public void fillCustomersForNewOrder(CustomersAdder uiAdapter) {
//        if (this.systemCustomers.getIterable() != null) {
//            for (SDMSystemCustomer customer : this.systemCustomers.getIterable()) {
//                uiAdapter.addCustomerToShow(customer);
//            }
//        }
//    }
//
//    public void filStoresForNewOrder(SDMNewOrderPage.UIAdapter uiAdapter) {
//        if (this.systemStores.getIterable() != null) {
//            for (Store store : this.systemStores.getIterable()) {
//                uiAdapter.addStoreToShow(store);
//            }
//        }
//    }

    public Store getCheapestSellerForItem(int itemId) {
        Store cheapestSeller = null;
        for (Store store : this.zoneStores.getIterable()) {
            if (store.getStoreItems().get(itemId) == null) {
                continue;
            } else if (cheapestSeller == null ||
                    store.getStoreItems().get(itemId).getPrice() < cheapestSeller.getStoreItems().get(itemId).getPrice()) {
                cheapestSeller = store;
            }
        }

        return cheapestSeller;
    }

    public void updateSummaryForDynamicOrder(StoresDetailsContainer dynamicOrderSummary, Store cheapestSeller, int itemId,
                                             Number amountValue, Location arrivalLocation) {
        if (isSellerExistInSummaryForDynamicOrder(dynamicOrderSummary, cheapestSeller)) {
            updateStoreSummaryForDynamicOrder(dynamicOrderSummary, cheapestSeller, itemId, amountValue);
        } else {
            addStoreSummaryForDynamicOrder(dynamicOrderSummary, cheapestSeller, itemId, amountValue, arrivalLocation);
        }
    }

    private void updateStoreSummaryForDynamicOrder(StoresDetailsContainer dynamicOrderSummary, Store cheapestSeller,
                                                   int itemId, Number amountValue) {
        StoreDetailsContainer storeInSummary = dynamicOrderSummary.getStore(cheapestSeller.getId());
        if (storeInSummary.isItemExist(itemId)) {
            ItemDetailsContainer itemDetailsContainer = storeInSummary.getItem(itemId);
            storeInSummary.setItemsIncome(storeInSummary.getItemsIncome() +
                    amountValue.floatValue() * cheapestSeller.getItemPrice(itemId) -
                    storeInSummary.getItem(itemId).getAmount() * cheapestSeller.getItemPrice(itemId));
            itemDetailsContainer.setAmount(amountValue.floatValue());
            if (itemDetailsContainer.getAmount() == 0) {
                storeInSummary.setNumberOfDifferentItems(storeInSummary.getNumberOfDifferentItems() - 1);
                if (storeInSummary.getNumberOfDifferentItems() == 0) {
                    removeStoreFromStoresDetailsContainer(dynamicOrderSummary, storeInSummary);
                }
            }
        } else {
            if (amountValue.floatValue() == 0) {
                return;
            }
            ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer(this.zoneItems.get(itemId));
            itemDetailsContainer.setPriceAtStore(cheapestSeller.getItemPrice(itemId));
            itemDetailsContainer.setAmount(amountValue.floatValue());

            storeInSummary.addItemDetailsContainer(itemDetailsContainer);
            storeInSummary.setItemsIncome(storeInSummary.getItemsIncome() +
                    amountValue.floatValue() * cheapestSeller.getItemPrice(itemId));
            storeInSummary.setNumberOfDifferentItems(storeInSummary.getNumberOfDifferentItems() + 1);
        }
    }

    private void removeStoreFromStoresDetailsContainer(StoresDetailsContainer dynamicOrderSummary, StoreDetailsContainer storeInSummary) {
        dynamicOrderSummary.getStoresDetailsContainer().removeIf(storeDetailsContainer -> storeDetailsContainer.getId() == storeInSummary.getId());
    }

    private void addStoreSummaryForDynamicOrder(StoresDetailsContainer dynamicOrderSummary, Store cheapestSeller,
                                                int itemId, Number amountValue, Location arrivalLocation) {
        if (amountValue.floatValue() == 0) {
            return;
        }
        StoreDetailsContainer storeDetailsContainer = new StoreDetailsContainer();
        storeDetailsContainer.setId(cheapestSeller.getId());
        storeDetailsContainer.setName(cheapestSeller.getName());
        storeDetailsContainer.setPpk(cheapestSeller.getPPK());
        storeDetailsContainer.setOwnerName(cheapestSeller.getOwner().getName());
        storeDetailsContainer.setDistanceFromCustomer(calcDistanceFromStore(cheapestSeller, arrivalLocation));
        storeDetailsContainer.setLocation(cheapestSeller.getLocation());
        storeDetailsContainer.setDeliveryPrice(cheapestSeller.getPPK() * storeDetailsContainer.getDistanceFromCustomer());
        storeDetailsContainer.setNumberOfDifferentItems(1);
        storeDetailsContainer.setItemsIncome(amountValue.floatValue() * cheapestSeller.getStoreItems().get(itemId).getPrice());

        ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer(this.zoneItems.get(itemId));
        itemDetailsContainer.setAmount(amountValue.floatValue());
        storeDetailsContainer.addItemDetailsContainer(itemDetailsContainer);

        dynamicOrderSummary.add(storeDetailsContainer);

    }

    private boolean isSellerExistInSummaryForDynamicOrder(StoresDetailsContainer dynamicOrderSummary, Store cheapestSeller) {
        for (StoreDetailsContainer storeDetailsContainer : dynamicOrderSummary.getStoresDetailsContainer()) {
            if (storeDetailsContainer.getId() == cheapestSeller.getId()) {
                return true;
            }
        }

        return false;
    }

    public void addItemToSellerForOrderContainer(OrderContainer orderContainer, Store selectedStore, Integer itemId, Number amount, Location location) {
        ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
        itemDetailsContainer.setId(itemId);
        itemDetailsContainer.setName(this.getItem(itemId).getName());
        itemDetailsContainer.setPurchaseCategory(this.zoneItems.get(itemId).getPurchaseCategory());
        itemDetailsContainer.setAmount(amount.floatValue());
        itemDetailsContainer.setPriceAtStore(selectedStore.getItemPrice(itemId));
        if (orderContainer.getSeller(selectedStore.getId()) != null) {
            orderContainer.getSeller(selectedStore.getId()).addItem(itemDetailsContainer);
        } else {
            ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
            itemsPerStoreContainer.addItem(itemDetailsContainer);
            itemsPerStoreContainer.setDeliveryPrice(getDeliveryPrice(selectedStore, location));
            itemsPerStoreContainer.setDistanceFromClient(calcDistanceFromStore(selectedStore, location));
            orderContainer.addSeller(selectedStore.getId(), itemsPerStoreContainer);
        }
    }

    public int analyzeOrderContainerTotalUnits(OrderContainer orderContainer) {
        int totalUnits = 0;
        for (Integer storeId : orderContainer.getStoreIdToSeller().keySet()) {
            totalUnits += orderContainer.getStoreIdToSeller().get(storeId).getNumberOfUnits();
        }

        return totalUnits;
    }

    public List<Discount> getRelevantDeals(Integer storeId, ItemsPerStoreContainer itemsPerStoreContainer) {
        List<Discount> discountsList = new ArrayList<>();
        Store store = this.zoneStores.get(storeId);
        for (ItemDetailsContainer item : itemsPerStoreContainer.getItems()) {
            List<Discount> storeDiscounts = store.getDiscountForItem(item);
            if (storeDiscounts.isEmpty() == false) {
                discountsList.addAll(storeDiscounts);
            }
        }

        return discountsList;
    }

    public Store getStore(Integer storeId) {
        return this.zoneStores.get(storeId);
    }

    public Collection<Locationable> getLocationableObjects() {
        List<Locationable> locationableList = new ArrayList<>();
        for (Store store : this.zoneStores.getIterable()) {
            locationableList.add(store);
        }
//        for (SDMSystemCustomer customer : this.systemCustomers.getIterable()) {
//            locationableList.add(customer);
//        }

        return locationableList;
    }

    public Point getMapSize() {
        int maxX = 0, maxY = 0;
        for (Locationable locationable : getLocationableObjects()) {
            if (maxX < locationable.getLocation().getX()) {
                maxX = locationable.getLocation().getX();
            }
            if (maxY < locationable.getLocation().getY()) {
                maxY = locationable.getLocation().getY();
            }
        }

        if (maxX != 50) {
            ++maxX;
        }
        if (maxY != 50) {
            ++maxY;
        }

        return new Point(maxX, maxY);
    }

    public Collection<Store> getStores() {
        List<Store> storeList = new ArrayList<>();
        for (Store store : this.zoneStores.getIterable()) {
            storeList.add(store);
        }

        return storeList;
    }

    public void addNewDiscountToStore(Store store, Discount newDeal) {
        store.addNewDeal(newDeal);
    }

    public int getNumberOfStoresSellingTheItem(Integer itemId) {
        return this.zoneItems.getNumberOfStoresSellingTheItem(itemId);
    }

    public float getAvgItemPrice(Integer itemId) {
        return this.zoneItems.getAvgItemPrice(itemId);
    }

    public float getItemSoldCounter(Integer itemId) {
        return this.zoneItems.getItemSoldCounter(itemId);
    }

    public void addNewStore(SDMSystemSeller seller, String storeName, Location location, Integer ppk, ArrayList<LinkedTreeMap<String, String>> itemsList) {
        if (isTakenLocation(location)) {
            throw new LocationException("Store location is already taken");
        }
        zoneStores.add(this,seller,storeName, location, ppk,itemsList);
        this.numberOfOrders++;
        if(seller.getName().equals(this.owner.getName()) == false){
            NewStoreNotification storeNotification = new NewStoreNotification(seller.getName(),
                    storeName,location, this.numberOfDifferentItems, itemsList.size(), this.name);
            this.owner.addNotification(storeNotification);
        }
    }

    public void addLocationable(Location location, Store store) {
        this.zoneLocations.add(location, store);
    }

    public int generateNewItemId() {
        int maxId = -1;
        for(SDMZoneItem zoneItem : this.zoneItems.getIterable()){
            if(zoneItem.getId() > maxId){
                maxId = zoneItem.getId();
            }
        }

        return ++maxId;
    }

    public void addNewItem(SDMZoneItem zoneItem) {
        this.zoneItems.add(zoneItem.getId(), zoneItem);
        ++this.numberOfDifferentItems;
    }
}