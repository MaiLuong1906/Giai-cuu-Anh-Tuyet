package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.CartItem;
import model.Order;
import model.Product;
import model.User;
import service.OrderService;
import service.EmailService;
import service.ProductService;

@WebServlet("/checkout")
public class CheckOutServlet extends HttpServlet {
    private OrderService orderService = new OrderService();
    private ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart/cart.jsp?error=EmptyCart");
            return;
        }

        // Kiểm tra tồn kho trước khi thanh toán
        for (CartItem item : cart) {
            Product latestProduct = productService.getProductById(item.getProduct().getId());
            if (latestProduct == null || latestProduct.getStock() < item.getQuantity()) {
                String productName = (latestProduct != null) ? latestProduct.getName() : "Sản phẩm không tồn tại";
                int available = (latestProduct != null) ? latestProduct.getStock() : 0;
                String errorMsg = java.net.URLEncoder.encode(
                        "Không đủ hàng tồn kho! \"" + productName + "\" chỉ còn " + available
                                + " sản phẩm, bạn yêu cầu " + item.getQuantity() + ".",
                        "UTF-8");
                response.sendRedirect(request.getContextPath() + "/carts?error=" + errorMsg);
                return;
            }
        }

        double total = 0;
        for (CartItem item : cart) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        double finalPrice = total;
        if (total > 1000000) {
            finalPrice = total * 0.9;
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalPrice(finalPrice);
        order.setStatus("Success");

        boolean isSuccess = orderService.saveFullOrder(order, cart);

        if (isSuccess) {
            // Giảm tồn kho sau khi đặt hàng thành công
            try {
                for (CartItem item : cart) {
                    productService.decreaseStock(item.getProduct().getId(), item.getQuantity());
                }
            } catch (Exception e) {
                System.out.println("Warning: Failed to decrease stock: " + e.getMessage());
                e.printStackTrace();
            }

            // Send order confirmation email
            EmailService.sendOrderConfirmation(user.getEmail(), finalPrice);

            session.removeAttribute("cart");
            request.setAttribute("finalPrice", finalPrice);
            request.getRequestDispatcher("/success.jsp").forward(request, response);
        } else {
            response.sendRedirect("cart/cart.jsp?error=DatabaseError");
        }
    }
}
