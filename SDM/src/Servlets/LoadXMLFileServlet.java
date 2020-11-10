package Servlets;

import Constants.Constants;
import LoadXMLDTO.LoadXMLDTO;
import SDMSystem.SDMSystem;
import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class LoadXMLFileServlet extends HttpServlet {

    private static final String FILE_CONTENT = "fileContent";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        synchronized (getServletContext()) {
            LoadXMLDTO loadXMLDTO = new LoadXMLDTO();
            if (getServletContext().getAttribute(Constants.SDM_SYSTEM) == null) {
                loadXMLDTO.setValid(false);
                loadXMLDTO.setErrorMessage("Unknown error occurred while loading XML file");

                return;
            }

            SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
            StringBuilder errorMessage = new StringBuilder();

            loadXMLDTO.setValid(sdmSystem.loadFile(req.getParameter(FILE_CONTENT), req.getParameter(Constants.USER_NAME_PARAM), errorMessage));
            loadXMLDTO.setErrorMessage(errorMessage.toString());

            Gson gson = new Gson();
            String json = gson.toJson(loadXMLDTO);

            try (PrintWriter out = resp.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        processRequest(req, resp);
    }
}
