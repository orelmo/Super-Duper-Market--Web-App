package Servlets;

import Constants.Constants;
import SDMSystem.SDMSystem;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetZonesServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(getServletContext().getAttribute(Constants.SDM_SYSTEM) == null){
            return;
        }
        SDMSystem mySystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        Gson gson = new Gson();
        String json;
        synchronized (getServletContext()) {
            json = gson.toJson(mySystem.getZonesDTO());
        }
        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
