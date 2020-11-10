package SDMSystem;

import EngineClasses.Store.SellerStores;
import EngineClasses.Store.Store;
import EngineClasses.Users.Notifications.Notification;
import EngineClasses.Users.Notifications.Notifications;
import EngineClasses.Users.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SDMSystemSeller extends User {
    private Notifications notifications = new Notifications();
    private SellerStores sellerStores = new SellerStores();

    public SDMSystemSeller() {
    }

    public SDMSystemSeller(User newUser) {
        super(newUser);
    }


    public void getPayment(float payment, Date transactionDate) {
        this.financialManager.getPayment(payment, transactionDate);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void addStore(String zoneName, Store newStore) {
        this.sellerStores.add(zoneName, newStore);
    }

    public List<Store> getZoneStores(String zoneName) {
        List<Store> storeList = new ArrayList<>();
        for(Store store : this.sellerStores.get(zoneName).getIterable()){
            storeList.add(store);
        }

        return storeList;
    }
}