package Servlets;

import Constants.Constants;
import EngineClasses.Item.Item;
import EngineClasses.Item.ePurchaseCategory;
import EngineClasses.Store.Sell;
import SDMSystem.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class NewItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String itemName = request.getParameter(Constants.ITEM_NAME);
        Integer itemPrice = Integer.parseInt(request.getParameter(Constants.ITEM_PRICE));
        ePurchaseCategory itemPurchaseCategory = request.getParameter(Constants.PURCHASE_CATEGORY).equals(
                ePurchaseCategory.Quantity.name()) ? ePurchaseCategory.Quantity : ePurchaseCategory.Weight;
        SDMZone zone = sdmSystem.getSystemZones().get(request.getParameter(Constants.ZONE_NAME));
        ArrayList<String> selectedStoresIds = gson.fromJson(request.getParameter(Constants.SELECTED_STORES_IDS), ArrayList.class);

        synchronized (getServletContext()) {
            int newItemId = zone.generateNewItemId();
            SDMZoneItem zoneItem = new SDMZoneItem(new Item(itemName, itemPurchaseCategory, newItemId));
            zone.addNewItem(zoneItem);

            for (String storeIdString : selectedStoresIds) {
                int storeId = Integer.parseInt(storeIdString);
                zone.addItemToStore(storeId, newItemId, itemPrice);
            }
        }
    }
}