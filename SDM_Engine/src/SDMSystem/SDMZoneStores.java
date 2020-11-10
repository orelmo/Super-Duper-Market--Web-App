package SDMSystem;

import EngineClasses.Interfaces.Containable;
import EngineClasses.Location.Location;
import EngineClasses.Store.Store;
import com.google.gson.internal.LinkedTreeMap;

import javax.naming.directory.NoSuchAttributeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SDMZoneStores implements Containable<Integer, Store> {
    private final Map<Integer, Store> storeIdToStore;

    public SDMZoneStores() {
        this.storeIdToStore = new HashMap<>();
    }

    public boolean isStoreSellingItem(int storeId, int itemId) {
        return this.get(storeId).isSellingItem(itemId);
    }

    @Override
    public boolean isExist(Integer id) {
        return this.storeIdToStore.containsKey(id);
    }

    @Override
    public Store get(Integer id) {
        return this.storeIdToStore.get(id);
    }

    @Override
    public void add(Integer id, Store newStore) {
        this.storeIdToStore.put(id, newStore);
    }

    @Override
    public Iterable<Store> getIterable() {
        return this.storeIdToStore.values();
    }

    public int getItemPriceFromStore(int storeId, int itemId) {
        return this.storeIdToStore.get(storeId).getItemPrice(itemId);
    }

    public int getNumberOfStores() {
        return this.storeIdToStore.size();
    }

    public void add(SDMZoneStores zoneStores) {
        for (Store store : zoneStores.getIterable()) {
            this.storeIdToStore.put(store.getId(), store);
        }
    }

    public void add(SDMZone sdmZone, SDMSystemSeller seller, String storeName, Location location, Integer ppk, ArrayList<LinkedTreeMap<String, String>> itemsList) {
        Store newStore = new Store(sdmZone, seller, storeName, location, ppk, itemsList, generateNewStoreId());
        this.add(newStore.getId(), newStore);
        seller.addStore(sdmZone.getName(), newStore);
    }

    private int generateNewStoreId() {
        int maxId = -1;
        for (Integer storeId : this.storeIdToStore.keySet()) {
            if (maxId < storeId) {
                maxId = storeId;
            }
        }

        return maxId + 1;
    }
}