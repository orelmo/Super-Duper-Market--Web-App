package EngineClasses.Store;

import EngineClasses.Interfaces.Containable;
import SDMSystem.SDMZoneStores;

import java.util.HashMap;
import java.util.Map;

public class SellerStores implements Containable<String, SDMZoneStores> {
    private Map<String, SDMZoneStores> stores = new HashMap<>();

    @Override
    public boolean isExist(String zoneName) {
        return this.stores.containsKey(zoneName);
    }

    @Override
    public SDMZoneStores get(String zoneName) {
        return this.stores.get(zoneName);
    }

    @Override
    public void add(String zoneName, SDMZoneStores zoneStores) {
        if (this.stores.containsKey(zoneName)) {
            this.stores.get(zoneName).add(zoneStores);
        } else {
            this.stores.put(zoneName, zoneStores);
        }
    }

    public void add(String zoneName, Store store) {
        if(this.stores.get(zoneName) == null){
            SDMZoneStores sdmZoneStores = new SDMZoneStores();
            sdmZoneStores.add(store.getId(), store);
            this.stores.put(zoneName, sdmZoneStores);
        } else {
            this.stores.get(zoneName).add(store.getId(), store);
        }
    }

    @Override
    public Iterable<SDMZoneStores> getIterable() {
        return this.stores.values();
    }
}