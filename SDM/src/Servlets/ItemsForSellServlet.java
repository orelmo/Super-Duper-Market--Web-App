package Servlets;

import Constants.Constants;
import EngineClasses.Store.Store;
import ItemsDetailsContainer.ItemDetailsContainer;
import SDMSystem.SDMSystem;
import StoresDetailsConteiner.StoresDetailsContainer;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ItemsForSellServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SDMSystem mySystem = (SDMSystem)getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        List<ItemDetailsContainer> itemDetailsContainerList;
        StoresDetailsContainer storesDetailsContainer = new StoresDetailsContainer();
        synchronized (getServletContext()) {
            if (request.getParameter(Constants.ORDER_TYPE).equals("Static Order")) {
                Store store = mySystem.getSystemZones().get(zoneName).getStore(Integer.parseInt(request.getParameter(Constants.STORE_ID)));
                mySystem.getSystemZones().get(zoneName).fillStoresDetailsContainer(storesDetailsContainer);
                itemDetailsContainerList = storesDetailsContainer.getStore(store.getId()).getItemsDetails();
            } else {
                itemDetailsContainerList = mySystem.getSystemZones().get(zoneName).getAllItemsDetails().getIteratable();
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(itemDetailsContainerList);
        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
