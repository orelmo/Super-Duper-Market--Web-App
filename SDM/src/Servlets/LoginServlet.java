package Servlets;

import EngineClasses.Users.UserManager;
import LoginDTO.LoginDTO;
import SDMSystem.SDMSystem;
import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static Constants.Constants.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        LoginDTO loginDTO = new LoginDTO();
        if (req.getSession(false) != null) {
            loginDTO.setError(false);
        } else {
            synchronized (this) {
                if (getServletContext().getAttribute(USER_MANAGER) == null) {
                    SDMSystem sdmSystem = new SDMSystem();
                    getServletContext().setAttribute(SDM_SYSTEM, sdmSystem);
                    UserManager userManager = new UserManager(sdmSystem);
                    getServletContext().setAttribute(USER_MANAGER,userManager );
                    sdmSystem.setUserManager(userManager);
                }
                if(req.getParameter(IS_FIRST_TRY)!=null &&  req.getParameter(IS_FIRST_TRY).equals("true")) {
                    loginDTO.setError(true);
                }
               else if (((UserManager) getServletContext().getAttribute(USER_MANAGER)).isExist(req.getParameter(USER_NAME_PARAM).trim())) {
                    loginDTO.setError(true);
                    loginDTO.setErrorMessage("User Name Is Already Exist");
                } else {
                    req.getSession(true).setAttribute(USER_NAME_PARAM,req.getParameter(USER_NAME_PARAM));
                    ((UserManager) getServletContext().getAttribute(USER_MANAGER)).add(req.getParameter(USER_NAME_PARAM).trim(), req.getParameter(USER_TYPE));
                    loginDTO.setError(false);
                    getServletContext().setAttribute(USER_TYPE, req.getAttribute(USER_TYPE));
                }
            }
        }

        String json = gson.toJson(loginDTO);
        resp.getWriter().println(json);
        resp.getWriter().flush();
    }
}