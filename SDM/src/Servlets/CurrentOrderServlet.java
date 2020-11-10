package Servlets;

import Constants.Constants;
import JsonOrderDTO.JsonOrderDTO;
import OrderConteiner.OrderContainer;
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

public class CurrentOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username= request.getParameter(Constants.USER_NAME_PARAM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        SDMSystemCustomer customer = sdmSystem.getCustomer(username);
        synchronized (getServletContext()) {
            zone.executeOrder(customer.getCurrentOrder());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username= request.getParameter(Constants.USER_NAME_PARAM);
        OrderContainer currentOrder = sdmSystem.getCustomer(username).getCurrentOrder();
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        JsonOrderDTO jsonOrderDTO = new JsonOrderDTO();
        Gson gson  = new Gson();

        jsonOrderDTO.fillJsonOrderDTO(currentOrder);
        jsonOrderDTO.setBoughtItemsStoresNames(getBoughtItemsStoresNames(sdmSystem.getSystemZones().get(zoneName)
                ,jsonOrderDTO.getBoughtItemsStoresIds()));

        String json = gson.toJson(jsonOrderDTO);
        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }

    private List<String> getBoughtItemsStoresNames(SDMZone sdmZone, List<Integer> boughtItemsStoresIds) {
        List<String> nameList = new ArrayList<>();
        for(Integer storeId : boughtItemsStoresIds){
            nameList.add(sdmZone.getStore(storeId).getName());
        }

        return nameList;
    }
}
