package SDMSystem;

import EngineClasses.Interfaces.Containable;

import java.util.HashMap;
import java.util.Map;

public class SDMZoneItems implements Containable<Integer, SDMZoneItem> {
    private final Map<Integer, SDMZoneItem> idToItem;

    public SDMZoneItems() {
        this.idToItem = new HashMap<>();
    }

    public SDMZoneItem get(Integer itemId) {
        return this.idToItem.get(itemId);
    }

    public boolean isExist(Integer itemId) {
        return this.idToItem.containsKey(itemId);
    }

    public void add(Integer itemId, SDMZoneItem newItem) {
        this.idToItem.put(itemId, newItem);
    }

    public Iterable<SDMZoneItem> getIterable() {
        return this.idToItem.values();
    }

    public void updateSDMItemDetails(int itemId, int newPrice) {
        this.idToItem.get(itemId).updateAvgPriceAfterAdding(newPrice);
        this.idToItem.get(itemId).increaseSellersCounter();
    }

    public int getNumberOfDifferentItems() {
        return this.idToItem.size();
    }

    public int getNumberOfStoresSellingTheItem(Integer itemId) {
        return this.idToItem.get(itemId).getNumberOfStoresSellingTheItem();
    }

    public float getAvgItemPrice(Integer itemId) {
        return this.idToItem.get(itemId).getAvgPrice();
    }

    public float getItemSoldCounter(Integer itemId) {
        return this.idToItem.get(itemId).getSoldCounter();
    }
}