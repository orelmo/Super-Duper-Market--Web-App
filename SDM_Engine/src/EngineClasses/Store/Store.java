package EngineClasses.Store;

import EngineClasses.Feedback.Feedback;
import EngineClasses.Feedback.Feedbacks;
import EngineClasses.Interfaces.Locationable;
import EngineClasses.Item.ePurchaseCategory;
import EngineClasses.Location.Location;
import EngineClasses.Order.DealItem;
import EngineClasses.Order.Order;
import EngineClasses.Order.StoreOfOrder;
import EngineClasses.Store.Discount.*;
import EngineClasses.Store.Discount.IfYouBuy;
import EngineClasses.Store.Discount.ThenYouGet;
import EngineClasses.Users.Notifications.NewFeedbackNotification;
import EngineClasses.Users.Notifications.NewOrderNotification;
import Exceptions.UnexistItemException;
import ItemsDetailsContainer.ItemDetailsContainer;
import SDMSystem.SDMSystemSeller;
import SDMSystem.SDMZone;
import Exceptions.IdAmbiguityException;
import Exceptions.LocationException;
import Exceptions.UndefinedItemException;
import com.google.gson.internal.LinkedTreeMap;
import jaxbClasses.*;
import SDMSystem.SDMOrdersHistory;

import javax.naming.directory.NoSuchAttributeException;
import java.util.*;

public class Store implements Locationable {
    private final int id;
    private String name;
    private SDMSystemSeller owner;
    private int deliveryPpk;
    private Location location;
    private StoreItems storeItems;
    private StoreDiscounts storeDiscounts;
    private SDMOrdersHistory ordersHistory;
    private float totalDeliveriesPayment;
    private int unitsSoldCounter;
    private SDMZone mySDMZone;
    private Feedbacks feedbacks;

    public Store(SDMStore SDMstore, SDMZone myZone) throws NoSuchAttributeException {
        this.name = SDMstore.getName();
        this.id = SDMstore.getId();
        this.deliveryPpk = SDMstore.getDeliveryPpk();
        setLocation(SDMstore.getLocation());
        this.totalDeliveriesPayment = 0;
        this.unitsSoldCounter = 0;
        this.mySDMZone = myZone;
        this.ordersHistory = new SDMOrdersHistory();
        this.storeItems = extractSDMPrices(SDMstore.getSDMPrices().getSDMSell());
        this.storeDiscounts = extractSDMDiscounts(SDMstore.getSDMDiscounts());
        this.owner = this.mySDMZone.getOwner();
        this.feedbacks = new Feedbacks();
    }

    public Store(SDMZone sdmZone, SDMSystemSeller seller, String storeName, Location location, Integer ppk, ArrayList<LinkedTreeMap<String, String>> itemsList, int id){
        this.owner = seller;
        this.name = storeName;
        this.location = location;
        this.deliveryPpk = ppk;
        this.id = id;
        this.mySDMZone = sdmZone;
        this.totalDeliveriesPayment = 0;
        this.unitsSoldCounter = 0;
        this.ordersHistory = new SDMOrdersHistory();
        this.feedbacks = new Feedbacks();
        this.storeDiscounts = new StoreDiscounts();
        this.storeItems = generateStoreItems(itemsList);

        sdmZone.addLocationable(location, this);

    }

    private StoreItems generateStoreItems(ArrayList<LinkedTreeMap<String, String>> itemsList) {
        StoreItems storeItems = new StoreItems();
        for(LinkedTreeMap<String, String> item : itemsList){
            Sell sell = new Sell(Integer.parseInt(item.get("id")),Integer.parseInt(item.get("price")));
            storeItems.add(sell.getItemId(), sell);
        }

        return storeItems;
    }

    public SDMZone getMySDMZone() {
        return this.mySDMZone;
    }

    public SDMSystemSeller getOwner() {
        return this.owner;
    }

    public void setOwner(SDMSystemSeller owner) {
        this.owner = owner;
    }

    private StoreDiscounts extractSDMDiscounts(SDMDiscounts sdmDiscounts) throws NoSuchAttributeException {
        if (sdmDiscounts == null) {
            return new StoreDiscounts();
        }
        StoreDiscounts storeDiscounts = new StoreDiscounts();
        for (SDMDiscount sdmDiscount : sdmDiscounts.getSDMDiscount()) {
            Discount discount = new Discount();
            discount.setStoreId(this.getId());
            String discountName = sdmDiscount.getName().trim();
            if (storeDiscounts.isExist(discountName)) {
                throw new IdAmbiguityException("There are more than one discount with the name: " + discountName + "in store " + this.name);
            }
            discount.setName(discountName);
            isValidDiscountItem(sdmDiscount.getIfYouBuy().getItemId(), this.mySDMZone.getItem(sdmDiscount.getIfYouBuy().getItemId()).getPurchaseCategory(),
                    sdmDiscount.getIfYouBuy().getQuantity(), sdmDiscount.getName());
            discount.setIfYouBuy(new IfYouBuy(sdmDiscount.getIfYouBuy()));
            for (SDMOffer sdmOffer : sdmDiscount.getThenYouGet().getSDMOffer()) {
                isValidDiscountItem(sdmOffer.getItemId(), this.mySDMZone.getItem(sdmOffer.getItemId()).getPurchaseCategory(), sdmOffer.getQuantity(),
                        sdmDiscount.getName());
            }
            try {
                discount.setThenYouGet(new ThenYouGet(sdmDiscount.getThenYouGet()));
            } catch (Exception e) {
                throw new NoSuchAttributeException("For discount with name " + discountName + " " + e.getMessage());
            }
            storeDiscounts.add(discount.getName(), discount);
        }
        for(Discount discount : storeDiscounts.getIterable()){
           for(Offer offer : discount.getBenefit().getOptionalBenefitItems().getIterable()){
               offer.setItemName(this.mySDMZone.getItem(offer.getItemId()).getName());
           }
        }
        return storeDiscounts;
    }

    private boolean isValidDiscountItem(int itemID, ePurchaseCategory purchaseCategory, double quantity, String discountName) {
        if (isSoldable(itemID)) {
            if (purchaseCategory.equals(ePurchaseCategory.Quantity)
                    && quantity % 1 > 0) {
                throw new NumberFormatException("The store " + this.name + "added a discount named" + discountName +
                        "but the quantity for item with id " + itemID + "must be an integer");
            }
        }

        return true;
    }

    public StoreDiscounts getStoreDiscounts() {
        return this.storeDiscounts;
    }

    private boolean isSoldable(int itemId) {
        if (this.mySDMZone.isItemExist(itemId) == false) {
            throw new UnexistItemException("Store with id: " + this.id + " trying to set a discount with item id: " + itemId +
                    " which is not in the system");
        } else if (this.isSellingItem(itemId) == false) {
            throw new UnexistItemException("Store with id: " + this.id + " trying to set a discount with item id: " + itemId +
                    " which is not in the store");
        }
        return true;
    }

    public SDMOrdersHistory getOrdersHistory() {
        return this.ordersHistory;
    }

    public StoreItems getStoreItems() {
        return this.storeItems;
    }

    public float getTotalDeliveriesPayment() {
        return this.totalDeliveriesPayment;
    }

    public Location getLocation() {
        return this.location;
    }

    public int getId() {
        return this.id;
    }

    public void setLocation(jaxbClasses.Location location) {
        try {
            this.location = new Location(location);
        } catch (LocationException e) {
            throw new LocationException("Store with id " + this.getId() + " " + e.getMessage());
        }
    }

    private StoreItems extractSDMPrices(List<SDMSell> sdmSell) {
        StoreItems storeItems = new StoreItems();
        for (SDMSell sell : sdmSell) {
            if (mySDMZone.isItemExist(sell.getItemId()) == false) {
                throw new UndefinedItemException("Store with id " + this.getId() + " tried to sell undefined item with id: " + sell.getItemId());
            }
            if (storeItems.isExist(sell.getItemId())) {
                throw new IdAmbiguityException("Store with id " + this.getId() +
                        " tried to sell the item with id " + sell.getItemId() + " more than once");
            }
            Sell newSell = new Sell(sell);
            storeItems.add(newSell.getItemId(), newSell);
            this.mySDMZone.updateSDMItemDetails(sell.getItemId(), sell.getPrice());
        }

        return storeItems;
    }

    public String getName() {
        return this.name;
    }

    public int getPPK() {
        return this.deliveryPpk;
    }

    public boolean isSellingItem(int itemId) {
        return this.storeItems.isExist(itemId);
    }

    public int getItemPrice(int itemId) {
        return this.storeItems.get(itemId).getPrice();
    }

    public int getUnitsSoldCounter() {
        return this.unitsSoldCounter;
    }

    public void increaseNumberOfSoldUnits(int amount) {
        this.unitsSoldCounter += amount;
    }

    public void addOrder(Order order) {
        this.ordersHistory.add(order);
    }

    public void updatePaymentForDelivery(float deliveryPrice) {
        this.totalDeliveriesPayment += deliveryPrice;
    }

    public void increaseStoreItemSoldCounter(int itemId, float amount) {
        this.storeItems.get(itemId).increaseSold(amount);
    }

    public void deleteItem(int itemId) {
        this.storeItems.deleteItem(itemId);
    }

    public void addItem(Sell sell) {
        if (this.storeItems.isExist(sell.getItemId())) {
            throw new IdAmbiguityException("The store is already selling the item with id: " + sell.getItemId());
        } else {
            this.storeItems.add(sell.getItemId(), sell);
        }
    }

    public void updateItemPrice(int itemId, int itemPrice) {
        if (this.storeItems.isExist(itemId) == false) {
            throw new UnexistItemException("The store doesn't have item with id: " + itemId);
        }
        this.storeItems.updateItemPrice(itemId, itemPrice);
    }

    public void updateSoldUnits(int unitsNumber) {
        this.unitsSoldCounter += unitsNumber;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + ", Name: " + this.name + ", Location: " + this.location.toString();
    }

    public void updateItemSoldCounter(int itemId, float amount) {
        this.storeItems.updateItemSoldCounter(itemId, amount);
    }

    public void increaseStoreItemsSoldCounter(StoreOfOrder storeOfOrder) {
        for(Integer itemId : storeOfOrder.getAmountForItem().getItemsId()){
            this.getStoreItems().get(itemId).increaseSold(storeOfOrder.getAmountForItem().get(itemId).floatValue());
        }
        for(DealItem dealItem : storeOfOrder.getDealItems()){
            this.getStoreItems().get(dealItem.getId()).increaseSold(dealItem.getAmount());
        }
    }

    public List<Discount> getDiscountForItem(ItemDetailsContainer item) {
        List<Discount> storeDiscounts = new ArrayList<>();
        for(Discount discount : this.storeDiscounts.getIterable()){
            if(discount.getTrigger().getItemIdNeedToBeBought() == item.getId()){
                float itemAmount = item.getAmount();
                while(itemAmount>=discount.getTrigger().getQuantityNeedToBeBought()){
                    storeDiscounts.add(discount);
                    itemAmount -= discount.getTrigger().getQuantityNeedToBeBought();
                }
            }
        }
        return storeDiscounts;
    }

    public boolean isDealNameExist(String dealName) {
        return this.storeDiscounts.isExist(dealName);
    }

    public void addNewDeal(Discount newDeal) {
        this.storeDiscounts.add(newDeal.getName(), newDeal);
    }

    public float analyzeItemsIncome(){
        float income = 0;
        for(Sell sell : this.storeItems.getIterable()){
            income += sell.getPrice() * sell.getSold();
        }

        return income;
    }

    public void getPayment(float payment, Date transactionDate) {
        this.owner.getPayment(payment, transactionDate);
    }

    public void notifyNewOrder(Order subOrder) {
        NewOrderNotification orderNotification = new NewOrderNotification(subOrder, this.name);
        this.owner.addNotification(orderNotification);
    }

    public void addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
        this.owner.addNotification(new NewFeedbackNotification(feedback));
    }

    public List<Feedback> getFeedbacks() {
        return this.feedbacks.getFeedbacks();
    }
}