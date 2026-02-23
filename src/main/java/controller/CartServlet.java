package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.CartItem;
import model.Product;
import java.util.List;
import service.ProductService;

@WebServlet(name = "CartServlet", urlPatterns = { "/carts" })
public class CartServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        this.productService = new ProductService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        request.getRequestDispatcher("cart/cart.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("remove".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                HttpSession session = request.getSession();
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

                if (cart != null) {
                    cart.removeIf(item -> item.getProduct().getId() == productId);
                }

                response.sendRedirect(request.getContextPath() + "/carts");
                return;
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/carts?error=Xóa sản phẩm thất bại");
                return;
            }
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Product product = productService.getProductById(productId);

            if (product != null) {
                HttpSession session = request.getSession();
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

                if (cart == null) {
                    cart = new ArrayList<>();
                    session.setAttribute("cart", cart);
                }

                // Tính số lượng đã có trong giỏ hàng
                int currentQuantityInCart = 0;
                CartItem existingItem = null;
                for (CartItem item : cart) {
                    if (item.getProduct().getId() == productId) {
                        currentQuantityInCart = item.getQuantity();
                        existingItem = item;
                        break;
                    }
                }

                // Kiểm tra tồn kho: số lượng muốn mua + đã có trong giỏ <= tồn kho
                int totalRequested = currentQuantityInCart + quantity;
                if (totalRequested > product.getStock()) {
                    int availableToAdd = product.getStock() - currentQuantityInCart;
                    String errorMsg = java.net.URLEncoder.encode(
                            "Không đủ hàng tồn kho! Sản phẩm \"" + product.getName()
                                    + "\" chỉ còn " + product.getStock() + " sản phẩm"
                                    + (currentQuantityInCart > 0 ? " (đã có " + currentQuantityInCart + " trong giỏ)"
                                            : "")
                                    + ". Bạn có thể thêm tối đa " + Math.max(0, availableToAdd) + " sản phẩm nữa.",
                            "UTF-8");
                    response.sendRedirect(request.getContextPath() + "/cart/productListCart.jsp?error=" + errorMsg);
                    return;
                }

                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                } else {
                    cart.add(new CartItem(product, quantity));
                }
            }
            response.sendRedirect(request.getContextPath() + "/carts");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cart/productListCart.jsp?error=Thêm vào giỏ thất bại");
        }
    }
}
