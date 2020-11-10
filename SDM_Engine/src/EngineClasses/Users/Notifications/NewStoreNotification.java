package EngineClasses.Users.Notifications;

import EngineClasses.Location.Location;

public class NewStoreNotification implements Notification {
    private String zoneName;
    private String newStoreOwnerName;
    private String newStoreName;
    private Location newStoreLocation;
    private int zoneDifferentItems;
    private int storeDifferentItems;
    private eNotificationType notificationType;

    public NewStoreNotification(String newStoreOwner, String newStoreName, Location newStoreLocation, int zoneDifferentItems
            , int storeDifferentItems, String zoneName) {
        this.zoneName = zoneName;
        this.newStoreOwnerName = newStoreOwner;
        this.newStoreName = newStoreName;
        this.newStoreLocation = newStoreLocation;
        this.zoneDifferentItems = zoneDifferentItems;
        this.storeDifferentItems = storeDifferentItems;
        this.notificationType = eNotificationType.NEW_STORE;
    }
}