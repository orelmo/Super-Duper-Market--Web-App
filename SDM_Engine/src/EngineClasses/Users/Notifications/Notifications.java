package EngineClasses.Users.Notifications;

import java.util.ArrayList;
import java.util.List;

public class Notifications {
    List<Notification> notificationList = new ArrayList<>();

    public void add(Notification notification) {
        this.notificationList.add(notification);
    }

    public List<Notification> getDeltaNotifications(Integer seenNotifications) {
        List<Notification> delta = new ArrayList<>();
        for (int j = 0; j < this.notificationList.size(); ++j) {
            if (j >= seenNotifications) {
                delta.add(this.notificationList.get(j));
            }
        }

        return delta;
    }
}