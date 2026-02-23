package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import userDao.UserDAO;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import model.User;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDAO userService;

    public void init() {
        userService = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String role = request.getParameter("role");
            String password = request.getParameter("password");
            Date dob = Date.valueOf(request.getParameter("dob"));
            
            if (role != null) {
                role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
            }
            
            User newUser = new User(0, username, email, country, role, true, password, dob);
            userService.insertUser(newUser);

            request.getSession().setAttribute("message", "User added successfully");
            response.sendRedirect("users");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format (required: yyyy-MM-dd)");
            showNewForm(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String role = request.getParameter("role");
            String password = request.getParameter("password");
            Date dob = Date.valueOf(request.getParameter("dob"));
            
            if (role != null) {
                role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
            }
            
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            User user = new User(id, username, email, country, role, status, password, dob);
            userService.updateUser(user);

            request.getSession().setAttribute("message", "User updated successfully");
            response.sendRedirect("users");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format (required: yyyy-MM-dd)");
            showEditForm(request, response);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/createUser.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userService.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/editUser.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        userService.deleteUser(id);
        request.getSession().setAttribute("message", "User deleted successfully");
        response.sendRedirect("users");
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> listUser = userService.selectAllUsers();
        request.setAttribute("listUser", listUser);

        if (request.getSession().getAttribute("message") != null) {
            request.setAttribute("message", request.getSession().getAttribute("message"));
            request.getSession().removeAttribute("message");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("user/listUser.jsp");
        dispatcher.forward(request, response);
    }
}
