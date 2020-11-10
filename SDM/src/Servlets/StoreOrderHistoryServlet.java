package Servlets;

import Constants.Constants;
import EngineClasses.Order.Order;
import EngineClasses.Store.Store;
import JsonOrderDTO.JsonOrderDTO;
import SDMSystem.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class StoreOrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username = request.getParameter(Constants.USER_NAME_PARAM);
        int storeId = Integer.parseInt(request.getParameter(Constants.STORE_ID));
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        synchronized (getServletContext()) {
            SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
            Store store = zone.getStore(storeId);
            SDMOrdersHistory ordersHistory = store.getOrdersHistory();

            List<JsonOrderDTO> ordersHistoryList = new ArrayList<>();
            for (Order order : ordersHistory.getIterable()) {
                ordersHistoryList.add(new JsonOrderDTO(order));
            }

            Gson gson = new Gson();
            String json = gson.toJson(ordersHistoryList);
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }
}