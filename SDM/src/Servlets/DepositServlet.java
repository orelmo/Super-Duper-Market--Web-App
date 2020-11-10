package Servlets;

import Constants.Constants;
import EngineClasses.Users.Transaction;
import EngineClasses.Users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;


public class DepositServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException {
        UserManager userManager = (UserManager) getServletContext().getAttribute(Constants.USER_MANAGER);
        String username = (String) request.getSession(false).getAttribute(Constants.USER_NAME_PARAM);
        try {
            synchronized (this) {
                userManager.depositToUser(username, request.getParameter(Constants.AMOUNT), request.getParameter(Constants.DATE));
            }
        } catch (ParseException e) {
            throw new ServletException("Date parse failed");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        processRequest(request,response);
    }
}
