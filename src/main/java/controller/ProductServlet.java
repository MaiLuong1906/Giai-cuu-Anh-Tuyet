package controller;

import model.Product;
import service.ProductService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = "/products")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertProduct(request, response);
                    break;
                case "edit":
                    updateProduct(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
                    deleteProduct(request, response);
                    break;
                default:
                    listProduct(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String description = request.getParameter("description");
            Date importDate = Date.valueOf(request.getParameter("import_date"));

            if (price < 0 || stock < 0) {
                request.setAttribute("error", "Price and stock must be >= 0");
                showNewForm(request, response);
                return;
            }

            Product newProduct = new Product(0, name, price, description, stock, importDate, true);
            productService.addProduct(newProduct);

            request.getSession().setAttribute("message", "Product added successfully");
            response.sendRedirect("products");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Price and stock must be valid numbers");
            showNewForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format (yyyy-MM-dd)");
            showNewForm(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String description = request.getParameter("description");
            Date importDate = Date.valueOf(request.getParameter("import_date"));
            boolean status = request.getParameter("status") != null;

            if (price < 0 || stock < 0) {
                request.setAttribute("error", "Price and stock must be >= 0");
                showEditForm(request, response);
                return;
            }

            Product product = new Product(id, name, price, description, stock, importDate, status);
            productService.modifyProduct(product);

            request.getSession().setAttribute("message", "Product updated successfully");
            response.sendRedirect("products");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Price and stock must be valid numbers");
            showEditForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format (yyyy-MM-dd)");
            showEditForm(request, response);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/createProduct.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product existingProduct = productService.getProductById(id);
        request.setAttribute("product", existingProduct);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/editProduct.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));

        Product product = productService.getProductById(id);
        if (product != null) {
            product.setStatus(false);
            productService.modifyProduct(product);
        }

        request.getSession().setAttribute("message", "Product marked as inactive successfully");
        response.sendRedirect("products");
    }

    private void listProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageSize = 10;
        int page = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        List<Product> productList = productService.getProductsByPage(offset, pageSize);

        System.out.println("DEBUG productList size = " + productList.size());

        int totalProducts = productService.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        request.setAttribute("productList", productList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("product/listProduct.jsp").forward(request, response);
    }
}
