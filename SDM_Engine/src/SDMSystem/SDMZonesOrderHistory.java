package SDMSystem;

import EngineClasses.Interfaces.Containable;
import EngineClasses.Order.Order;

import java.util.HashMap;
import java.util.Map;

public class SDMZonesOrderHistory implements Containable<String,SDMOrdersHistory> {
    private Map<String, SDMOrdersHistory> zoneNameToZoneOrderHistory = new HashMap<String, SDMOrdersHistory>();

    @Override
    public boolean isExist(String zoneName) {
        return this.zoneNameToZoneOrderHistory.containsKey(zoneName);
    }

    @Override
    public SDMOrdersHistory get(String zoneName) {
        if (this.zoneNameToZoneOrderHistory.containsKey(zoneName) == false) {
            return new SDMOrdersHistory();
        }
        return this.zoneNameToZoneOrderHistory.get(zoneName);
    }

    @Override
    public Iterable<SDMOrdersHistory> getIterable() {
        return this.zoneNameToZoneOrderHistory.values();
    }

    @Override
    public void add(String zoneName, SDMOrdersHistory zoneOrderHistory) {
        if (this.zoneNameToZoneOrderHistory.containsKey(zoneName)) {
            this.zoneNameToZoneOrderHistory.get(zoneName).add(zoneOrderHistory);
        } else {
            this.zoneNameToZoneOrderHistory.put(zoneName, zoneOrderHistory);
        }
    }

    public void add(String zoneName, Order order) {
        if (this.zoneNameToZoneOrderHistory.containsKey(zoneName)) {
            this.zoneNameToZoneOrderHistory.get(zoneName).add(order);
        } else {
            SDMOrdersHistory ordersHistory = new SDMOrdersHistory();
            ordersHistory.add(order);
            this.zoneNameToZoneOrderHistory.put(zoneName, ordersHistory);
        }
    }
}