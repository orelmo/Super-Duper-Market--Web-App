package SDMSystem;

import EngineClasses.Interfaces.Containable;
import ZoneDTO.ZoneDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SDMZones implements Containable<String, SDMZone> {

    private Map<String, SDMZone> zoneNameToZone = new HashMap<>();

    @Override
    public boolean isExist(String zoneName) {
        return this.zoneNameToZone.containsKey(zoneName);
    }

    @Override
    public SDMZone get(String zoneName) {
        return this.zoneNameToZone.get(zoneName);
    }

    @Override
    public void add(String zoneName, SDMZone newZone) {
        this.zoneNameToZone.put(zoneName, newZone);
    }

    @Override
    public Iterable<SDMZone> getIterable() {
        return this.zoneNameToZone.values();
    }

    public List<ZoneDTO> getZoneDTOList() {
        List<ZoneDTO> zoneDTOList = new ArrayList<>();
        for (SDMZone zone : this.zoneNameToZone.values()) {
            ZoneDTO zoneDTO = new ZoneDTO();
            zoneDTO.setZoneName(zone.getName());
            zoneDTO.setOwnerName(zone.getOwner().getName());
            zoneDTO.setNumberOfStores(zone.getNumberOfStores());
            zoneDTO.setNumberOfDifferentItems(zone.getNumberOfDifferentItems());
            zoneDTO.setNumberOfOrders(zone.getNumberOfOrders());
            zoneDTO.setAvgOrderItemsPrice(zone.getAvgOrderItemsPrice());

            zoneDTOList.add(zoneDTO);
        }

        return zoneDTOList;
    }
}