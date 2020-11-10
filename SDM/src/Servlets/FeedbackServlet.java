package Servlets;

import Constants.Constants;
import EngineClasses.Customer.Customer;
import EngineClasses.Feedback.Feedback;
import EngineClasses.Store.Store;
import SDMSystem.*;
import StoresDetailsConteiner.StoreDetailsContainer;
import StoresDetailsConteiner.StoresDetailsContainer;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        Integer storeId = Integer.parseInt(request.getParameter(Constants.STORE_ID));
        String username  = request.getParameter(Constants.USER_NAME_PARAM);
        Integer rate  = Integer.parseInt(request.getParameter(Constants.RATE));
        String message  = request.getParameter(Constants.MESSAGE);
        SDMSystemCustomer customer = sdmSystem.getCustomer(username);
        Date date = gson.fromJson(request.getParameter(Constants.DATE), Date.class);

        Feedback feedback = new Feedback(rate, message, customer, date);

        Store store = zone.getStore(storeId);
        synchronized (getServletContext()) {
            store.addFeedback(feedback);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username  = request.getParameter(Constants.USER_NAME_PARAM);
        SDMSystemCustomer customer = sdmSystem.getCustomer(username);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);

        List<Integer> feedbackableStoresIds;
        synchronized (getServletContext()) {
            feedbackableStoresIds = customer.getFeedbackableStores();
        }
        List<StoreDetailsContainer> feedbackableStores = transformStoreIdToStoreDetailContainer(feedbackableStoresIds, zone);

        Gson gson = new Gson();
        String json = gson.toJson(feedbackableStores);

        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }

    private List<StoreDetailsContainer> transformStoreIdToStoreDetailContainer(List<Integer> feedbackableStoresIds,SDMZone zone) {
        List<StoreDetailsContainer> storeList =new ArrayList<>();
        for(Integer storeId : feedbackableStoresIds){
            StoreDetailsContainer storeDetailsContainer = new StoreDetailsContainer();
            storeDetailsContainer.setId(storeId);
            storeDetailsContainer.setName(zone.getStore(storeId).getName());
            storeList.add(storeDetailsContainer);
        }

        return storeList;
    }
}
