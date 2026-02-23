<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
            <%@ page import="productDao.ProductDAO" %>
                <%@ page import="model.Product" %>
                    <%@ page import="java.util.List" %>
                        <%@ page import="java.util.stream.Collectors" %>
                            <jsp:useBean id="productDAO" class="productDao.ProductDAO" scope="page" />
                            <% /* Only show active products (status=true) */ List<Product> allProducts =
                                productDAO.selectAllProducts();
                                List<Product> activeProducts = allProducts.stream()
                                    .filter(Product::isStatus)
                                    .collect(Collectors.toList());
                                    request.setAttribute("products", activeProducts);
                                    %>

                                    <fmt:setLocale value="vi_VN" />
                                    <html>

                                    <head>
                                        <meta charset="UTF-8">
                                        <title>Danh Sách Sản Phẩm</title>
                                        <style>
                                            body {
                                                font-family: Arial, sans-serif;
                                                text-align: center;
                                            }

                                            .product {
                                                border: 1px solid #ddd;
                                                padding: 10px;
                                                margin: 10px;
                                                display: inline-block;
                                                width: 250px;
                                            }

                                            button {
                                                background: #28a745;
                                                color: white;
                                                padding: 5px;
                                                border: none;
                                                cursor: pointer;
                                            }
                                        </style>
                                    </head>

                                    <body>
                                        <h2>Danh Sách Sản Phẩm</h2>
                                        <a href="<%= request.getContextPath()%>/cart/cart.jsp">🛒 Xem Giỏ
                                            Hàng</a><br><br>

                                        <!-- Hiển thị thông báo lỗi (ví dụ: hết hàng) -->
                                        <c:if test="${not empty param.error}">
                                            <p
                                                style="color: red; font-weight: bold; background: #ffe0e0; padding: 10px; border-radius: 5px; display: inline-block;">
                                                ⚠️ ${param.error}
                                            </p><br><br>
                                        </c:if>

                                        <c:forEach var="product" items="${products}">
                                            <div class="product">
                                                <h3>${product.name}</h3>
                                                <p>Giá: ${product.price} VND</p>
                                                <p>Tồn kho: <strong>${product.stock}</strong></p>
                                                <c:choose>
                                                    <c:when test="${product.stock > 0}">
                                                        <form action="<%= request.getContextPath()%>/carts"
                                                            method="post">
                                                            <input type="hidden" name="productId"
                                                                value="${product.id}" />
                                                            <input type="number" name="quantity" value="1" min="1"
                                                                max="${product.stock}" />
                                                            <button type="submit">Thêm vào Giỏ</button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p style="color: red; font-weight: bold;">Hết hàng</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                        <br>
                                        <a href="<%= request.getContextPath()%>/welcome.jsp">Back to welcome</a>
                                    </body>

                                    </html>