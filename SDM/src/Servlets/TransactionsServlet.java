package Servlets;

import Constants.Constants;
import EngineClasses.Users.Transaction;
import EngineClasses.Users.Transactions;
import EngineClasses.Users.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TransactionsServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserManager userManager = (UserManager) getServletContext().getAttribute(Constants.USER_MANAGER);
        String username = (String)request.getSession(false).getAttribute(Constants.USER_NAME_PARAM);
        synchronized (getServletContext()) {
            List<Transaction> transactions = userManager.getTransactionsList(username);
            Gson gson = new Gson();
            String json = gson.toJson(transactions);

            try (PrintWriter out = response.getWriter()) {
                out.println(json);
                out.flush();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }
}
