package Servlets;

import EngineClasses.Users.User;
import EngineClasses.Users.UserManager;
import OnlineUsersDTO.OnlineUsersDTO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static Constants.Constants.*;

public class OnlineUsersServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OnlineUsersDTO onlineUsersDTO = new OnlineUsersDTO();
        UserManager userManager = (UserManager) getServletContext().getAttribute(USER_MANAGER);

        Map<String,String> users = new HashMap<>();
        for(User user : userManager.getUsers().values()){
            users.put(user.getName(),user.getUserType().toString());
        }
        onlineUsersDTO.setSuccess(true);
        onlineUsersDTO.setUsers(users);

        Gson gson = new Gson();
        String json = gson.toJson(onlineUsersDTO);

        try(PrintWriter out = response.getWriter()){
            out.println(json);
            out.flush();
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
