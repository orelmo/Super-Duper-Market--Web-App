package Servlets;

import Constants.Constants;
import EngineClasses.Users.Notifications.Notification;
import EngineClasses.Users.Notifications.Notifications;
import SDMSystem.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificationsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username= request.getParameter(Constants.USER_NAME_PARAM);
        SDMSystemSeller seller = sdmSystem.getSeller(username);
        Integer seenNotifications;
        if(request.getParameter(Constants.SEEN_NOTIFICATIONS).equals("")){
            seenNotifications = 0;
        }else {
            seenNotifications = Integer.parseInt(request.getParameter(Constants.SEEN_NOTIFICATIONS));
        }

        List<Notification> notifications = new ArrayList<>();
        notifications.addAll(getDeltaNotifications(seller.getNotifications(), seenNotifications));
        seenNotifications += notifications.size();

        Gson gson = new Gson();
        String json ="[" +  gson.toJson(notifications) + ',' + gson.toJson(seenNotifications) + ']';

        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }

    private List<Notification> getDeltaNotifications(Notifications notifications, Integer seenNotifications) {
        return notifications.getDeltaNotifications(seenNotifications);
    }
}
