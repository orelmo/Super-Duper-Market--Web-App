package Servlets;

import Constants.Constants;
import EngineClasses.ChatManager.ChatManager;
import SDMSystem.SDMSystem;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        String newMessage = request.getParameter(Constants.NEW_MESSAGE);
        synchronized (this) {
            sdmSystem.addChatMessage(newMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SDMSystem sdmSystem = (SDMSystem) getServletContext().getAttribute(Constants.SDM_SYSTEM);
        Integer seenMessages = Integer.parseInt(request.getParameter(Constants.SEEN_MESSAGES));
        List<String> chatHistory = sdmSystem.getDeltaChatMessages(seenMessages);

        Gson gson = new Gson();
        String json = gson.toJson(chatHistory);

        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}