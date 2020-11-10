package Servlets;

import Constants.Constants;
import EngineClasses.Location.Location;
import EngineClasses.Store.Discount.Discount;
import EngineClasses.Store.Store;
import ItemsDetailsContainer.ItemDetailsContainer;
import OrderConteiner.ItemsPerStoreContainer;
import OrderConteiner.OrderContainer;
import SDMSystem.*;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DealsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String customerUsername = request.getParameter(Constants.USER_NAME_PARAM);
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);

        OrderContainer orderContainer = new OrderContainer();
        fillOrderContainer(orderContainer,request);

        synchronized (getServletContext()) {
            sdmSystem.getCustomer(customerUsername).setCurrentOrder(orderContainer);
        }
    }

    private void fillOrderContainer(OrderContainer orderContainer, HttpServletRequest request) {
        String customerUsername = request.getParameter(Constants.USER_NAME_PARAM);
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        Gson gson = new Gson();
        Date date = gson.fromJson(request.getParameter(Constants.DATE),Date.class);

        orderContainer.setCustomer(sdmSystem.getCustomer(customerUsername));
        orderContainer.setArrivalDate(date);
        orderContainer.setArrivalLocation(new Location(Integer.parseInt(request.getParameter(Constants.X_COORD))
                , Integer.parseInt(request.getParameter(Constants.Y_COORD))));

        fillBoughtItems(orderContainer,request);
        fillDealItems(orderContainer, request);
    }

    private void fillDealItems(OrderContainer orderContainer, HttpServletRequest request) {
        Gson gson = new Gson();
        ArrayList selectedDeals = gson.fromJson(request.getParameter(Constants.SELECTED_DEALS), ArrayList.class);

        OrderContainer dealsOrderContainer = new OrderContainer();
        fillDeals(dealsOrderContainer, selectedDeals, request);

        orderContainer.setStoreIdToDeals(dealsOrderContainer.getStoreIdToSeller());
        orderContainer.setItemsPrice(orderContainer.getItemsPrice() + dealsOrderContainer.getItemsPrice());
        orderContainer.setTotalOrderPrice(orderContainer.getTotalOrderPrice() + dealsOrderContainer.getItemsPrice());
    }

    private void fillDeals(OrderContainer dealsOrderContainer, ArrayList<LinkedTreeMap<String, Double>> selectedDeals,
                           HttpServletRequest request) {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        for(LinkedTreeMap<String, Double> item : selectedDeals){
            ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
            itemDetailsContainer.setId(item.get("itemId").intValue());
            itemDetailsContainer.setAmount(item.get("itemAmount").floatValue());
            itemDetailsContainer.setName(zone.getItem(item.get("itemId").intValue()).getName());
            itemDetailsContainer.setPurchaseCategory(zone.getItem(item.get("itemId").intValue()).getPurchaseCategory());
            itemDetailsContainer.setPriceAtStore(item.get("additionalPrice").intValue());

            if(dealsOrderContainer.getStoreIdToSeller().containsKey(item.get("storeId").intValue())){
                dealsOrderContainer.getStoreIdToSeller().get(item.get("storeId").intValue()).addItem(itemDetailsContainer);
            } else {
                ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
                itemsPerStoreContainer.addItem(itemDetailsContainer);

                dealsOrderContainer.getStoreIdToSeller().put(item.get("storeId").intValue(), itemsPerStoreContainer);
            }
        }

        for(ItemsPerStoreContainer itemsPerStoreContainer : dealsOrderContainer.getStoreIdToSeller().values()){
            dealsOrderContainer.setItemsPrice(dealsOrderContainer.getItemsPrice() + itemsPerStoreContainer.getItemsPrice());
        }
    }

    private void fillBoughtItems(OrderContainer orderContainer, HttpServletRequest request) {
        Gson gson = new Gson();
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        String orderType = request.getParameter(Constants.ORDER_TYPE);
        ArrayList<String> itemsIds = gson.fromJson(request.getParameter(Constants.ITEMS_IDS), ArrayList.class);
        ArrayList<String> itemsAmounts = gson.fromJson(request.getParameter(Constants.ITEMS_AMOUNTS), ArrayList.class);


        Map<Integer, Float> itemsIdToAmount = new HashMap<>();
        for (int i = 0; i < itemsIds.size(); ++i) {
            itemsIdToAmount.put(Integer.parseInt(itemsIds.get(i)), Float.parseFloat(itemsAmounts.get(i)));
            if (orderType.equals("Static Order")) {
                int storeId = Integer.parseInt(request.getParameter(Constants.STORE_ID));
                orderContainer.setItemsPrice(orderContainer.getItemsPrice() +
                        zone.getItemPriceFromStore(storeId, Integer.parseInt(itemsIds.get(i))) * Float.parseFloat(itemsAmounts.get(i)));
            } else {
                Store cheapestStore = zone.getCheapestSellerForItem(Integer.parseInt(itemsIds.get(i)));
                orderContainer.setItemsPrice(orderContainer.getItemsPrice() +
                        cheapestStore.getItemPrice(Integer.parseInt(itemsIds.get(i))) * Float.parseFloat(itemsAmounts.get(i)));
            }
        }

        if (orderType.equals("Static Order")) {
            for(Integer itemId : itemsIdToAmount.keySet()){
                zone.addItemToSellerForOrderContainer(orderContainer,zone.getStore(Integer.parseInt(request.getParameter(Constants.STORE_ID)))
                        , itemId, itemsIdToAmount.get(itemId),new Location(Integer.parseInt(request.getParameter(Constants.X_COORD))
                                , Integer.parseInt(request.getParameter(Constants.Y_COORD))));
            }
            orderContainer.setDeliveryPrice(zone.getDeliveryPrice(zone.getStore(Integer.parseInt(request.getParameter(Constants.STORE_ID)))
                    , new Location(Integer.parseInt(request.getParameter(Constants.X_COORD))
                            , Integer.parseInt(request.getParameter(Constants.Y_COORD)))));
        } else {
            Store cheapestStore;
            for (Integer itemId : itemsIdToAmount.keySet()) {
                cheapestStore = zone.getCheapestSellerForItem(itemId);
                if(orderContainer.getStoreIdToSeller().keySet().contains(cheapestStore.getId()) == false) {
                   orderContainer.setDeliveryPrice(orderContainer.getDeliveryPrice() +
                           zone.getDeliveryPrice(cheapestStore,new Location(Integer.parseInt(request.getParameter(Constants.X_COORD))
                                   , Integer.parseInt(request.getParameter(Constants.Y_COORD)))));
                }
               zone.addItemToSellerForOrderContainer(orderContainer, cheapestStore, itemId, itemsIdToAmount.get(itemId)
                       , new Location(Integer.parseInt(request.getParameter(Constants.X_COORD))
                       , Integer.parseInt(request.getParameter(Constants.Y_COORD))));
            }
        }

        for(Integer storeId : orderContainer.getStoreIdToSeller().keySet()){
            orderContainer.getStoreIdToSeller().get(storeId).setPPK(zone.getStore(storeId).getPPK());
        }

        orderContainer.setTotalUnits(zone.analyzeOrderContainerTotalUnits(orderContainer));
        orderContainer.setTotalOrderPrice(orderContainer.getDeliveryPrice() + orderContainer.getItemsPrice());
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        Gson gson = new Gson();
        ArrayList<String> itemsIds = gson.fromJson(request.getParameter(Constants.ITEMS_IDS), ArrayList.class);
        ArrayList<String> itemsAmounts = gson.fromJson(request.getParameter(Constants.ITEMS_AMOUNTS), ArrayList.class);
        String orderType = request.getParameter(Constants.ORDER_TYPE);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);

        List<Discount> allDeals = new ArrayList<>();
        if(orderType.equals("Dynamic Order")){
            Store cheapestStore;
            for(int i =0 ; i<itemsIds.size(); ++i){
                cheapestStore = zone.getCheapestSellerForItem(Integer.parseInt(itemsIds.get(i)));
                ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
                ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
                itemDetailsContainer.setId(Integer.parseInt(itemsIds.get(i)));
                itemDetailsContainer.setAmount(Float.parseFloat(itemsAmounts.get(i)));
                itemsPerStoreContainer.addItem(itemDetailsContainer);
                allDeals.addAll(zone.getRelevantDeals(cheapestStore.getId(),itemsPerStoreContainer));
            }
        }else{
            int storeId = Integer.parseInt(request.getParameter(Constants.STORE_ID));
            for(int i =0 ; i<itemsIds.size(); ++i){
                ItemsPerStoreContainer itemsPerStoreContainer = new ItemsPerStoreContainer();
                ItemDetailsContainer itemDetailsContainer = new ItemDetailsContainer();
                itemDetailsContainer.setId(Integer.parseInt(itemsIds.get(i)));
                itemDetailsContainer.setAmount(Float.parseFloat(itemsAmounts.get(i)));
                itemsPerStoreContainer.addItem(itemDetailsContainer);
                allDeals.addAll(zone.getRelevantDeals(storeId,itemsPerStoreContainer));
            }
        }

        String json = gson.toJson(allDeals);
        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }
}
