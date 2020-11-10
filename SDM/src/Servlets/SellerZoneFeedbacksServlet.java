package Servlets;

import Constants.Constants;
import EngineClasses.Feedback.Feedback;
import EngineClasses.Store.Store;
import FeedbackDTO.FeedbackDTO;
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

public class SellerZoneFeedbacksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String zoneName = request.getParameter(Constants.ZONE_NAME);
        String username = request.getParameter(Constants.USER_NAME_PARAM);

        synchronized (getServletContext()) {
            SDMSystemSeller seller = sdmSystem.getSeller(username);

            List<FeedbackDTO> feedbackDTOList = new ArrayList<FeedbackDTO>();
            for (Store store : seller.getZoneStores(zoneName)) {
                for (Feedback feedback : store.getFeedbacks()) {
                    FeedbackDTO feedbackDTO = new FeedbackDTO();
                    feedbackDTO.setCustomerName(feedback.getCustomer().getName());
                    feedbackDTO.setDate(feedback.getDate());
                    feedbackDTO.setMessage(feedback.getMessage());
                    feedbackDTO.setRate(feedback.getRate());
                    feedbackDTO.setStoreName(store.getName());

                    feedbackDTOList.add(feedbackDTO);
                }
            }

            Gson gson = new Gson();
            String json = gson.toJson(feedbackDTOList);

            try (PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }
}