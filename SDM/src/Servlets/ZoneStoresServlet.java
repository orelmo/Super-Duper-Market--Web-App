package Servlets;

import Constants.Constants;
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
import java.util.List;

public class ZoneStoresServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);

        synchronized (getServletContext()) {
            SDMZone zone = sdmSystem.getSystemZones().get(zoneName);

            StoresDetailsContainer storesDetailsContainer = new StoresDetailsContainer();
            zone.fillStoresDetailsContainer(storesDetailsContainer);

            Gson gson = new Gson();
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