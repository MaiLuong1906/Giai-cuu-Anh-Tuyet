package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import userDao.UserDAO;
import model.User;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userService;

    public void init() {
        userService = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }

        String rememberedUsername = null;
        String rememberedPassword = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberedUsername")) {
                    rememberedUsername = java.net.URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
                if (cookie.getName().equals("rememberedPassword")) {
                    rememberedPassword = java.net.URLDecoder.decode(cookie.getValue(), "UTF-8");
                }
            }
        }

        if (rememberedUsername != null) {
            request.setAttribute("rememberedUsername", rememberedUsername);
        }
        if (rememberedPassword != null) {
            request.setAttribute("rememberedPassword", rememberedPassword);
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        User user = userService.checkLogin(username, password);

        if (user != null) {
            if (!user.isStatus()) {
                request.setAttribute("error", "Your account has been inactive");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if (remember != null) {

                String encodedUsername = java.net.URLEncoder.encode(username, "UTF-8");
                Cookie usernameCookie = new Cookie("rememberedUsername", encodedUsername);
                usernameCookie.setMaxAge(30 * 24 * 60 * 60);
                usernameCookie.setHttpOnly(true);
                usernameCookie.setSecure(request.isSecure());
                usernameCookie.setPath(request.getContextPath() + "/");
                response.addCookie(usernameCookie);


                String encodedPassword = java.net.URLEncoder.encode(password, "UTF-8");
                Cookie passwordCookie = new Cookie("rememberedPassword", encodedPassword);
                passwordCookie.setMaxAge(30 * 24 * 60 * 60);
                passwordCookie.setHttpOnly(true);
                passwordCookie.setSecure(request.isSecure());
                passwordCookie.setPath(request.getContextPath() + "/");
                response.addCookie(passwordCookie);
            } else {

                Cookie usernameCookie = new Cookie("rememberedUsername", "");
                usernameCookie.setMaxAge(0);
                usernameCookie.setPath(request.getContextPath() + "/");
                response.addCookie(usernameCookie);

                Cookie passwordCookie = new Cookie("rememberedPassword", "");
                passwordCookie.setMaxAge(0);
                passwordCookie.setPath(request.getContextPath() + "/");
                response.addCookie(passwordCookie);
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect(request.getContextPath() + "/welcome.jsp");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
