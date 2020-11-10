package Servlets;

import Constants.Constants;
import EngineClasses.Customer.Customer;
import EngineClasses.Location.Location;
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
import java.util.HashMap;
import java.util.List;

public class OrderSuppliersServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        String xCoord = request.getParameter(Constants.X_COORD);
        String yCoord = request.getParameter(Constants.Y_COORD);
        Gson gson = new Gson();
        String itemsIdsStrings = request.getParameter("itemsIds");
        String amountsStrings = request.getParameter("amounts");
        ArrayList<String> itemsIds = gson.fromJson(itemsIdsStrings, ArrayList.class);
        ArrayList<String> amounts = gson.fromJson(amountsStrings, ArrayList.class);
        StoresDetailsContainer storesDetailsContainer = new StoresDetailsContainer();

        synchronized (getServletContext()) {
            for (int i = 0; i < itemsIds.size(); ++i) {
                int itemId = Integer.parseInt(itemsIds.get(i));
                zone.updateSummaryForDynamicOrder(storesDetailsContainer,
                        zone.getCheapestSellerForItem(itemId), itemId,
                        Float.valueOf(amounts.get(i)), new Location(Integer.parseInt(xCoord), Integer.parseInt(yCoord)));
            }

            String json = gson.toJson(storesDetailsContainer.getStoresDetailsContainer());
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
