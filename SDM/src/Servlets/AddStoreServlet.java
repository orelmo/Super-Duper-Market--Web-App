package Servlets;

import Constants.Constants;
import EngineClasses.Location.Location;
import Exceptions.LocationException;
import SDMSystem.*;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AddStoreServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String username= request.getParameter(Constants.USER_NAME_PARAM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        SDMZone zone = sdmSystem.getSystemZones().get(zoneName);
        String storeName = request.getParameter(Constants.STORE_NAME);
        Integer xCoord = Integer.parseInt(request.getParameter(Constants.X_COORD));
        Integer yCoord = Integer.parseInt(request.getParameter(Constants.Y_COORD));
        Integer PPK = Integer.parseInt(request.getParameter(Constants.PPK));
        Gson gson = new Gson();
        String itemsListString = request.getParameter(Constants.ITEMS_LIST);
        ArrayList<LinkedTreeMap<String, String>> itemsList = gson.fromJson(itemsListString, ArrayList.class);
        String message = "success";
        try{
            synchronized (this) {
                zone.addNewStore(sdmSystem.getSeller(username), storeName, new Location(xCoord, yCoord), PPK, itemsList);
            }
        }catch (LocationException e){
            message = e.getMessage();
        }
        try(PrintWriter out = response.getWriter()){
            out.println(gson.toJson(message));
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
