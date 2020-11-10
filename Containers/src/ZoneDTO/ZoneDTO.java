package ZoneDTO;

public class ZoneDTO {
    private String zoneName;
    private String ownerName;
    private int numberOfDifferentItems;
    private int numberOfStores;
    private int numberOfOrders;
    private float avgOrderItemsPrice;

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setNumberOfDifferentItems(int numberOfDifferentItems) {
        this.numberOfDifferentItems = numberOfDifferentItems;
    }

    public void setNumberOfStores(int numberOfStores) {
        this.numberOfStores = numberOfStores;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public void setAvgOrderItemsPrice(float avgOrderItemsPrice) {
        this.avgOrderItemsPrice = avgOrderItemsPrice;
    }
}