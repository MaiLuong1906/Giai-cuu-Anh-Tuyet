<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="vi_VN"/>
<html>
    <head>
        <title>Giỏ hàng</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            .total-row {
                font-weight: bold;
                background-color: #f2f2f2;
            }
            .action-link {
                color: red;
                text-decoration: none;
            }
            .action-link:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <h2>Giỏ hàng của bạn</h2>
        <a href="${pageContext.request.contextPath}/cart/productListCart.jsp">Tiếp tục mua hàng</a>
        <br><br>

        <table>
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Tổng</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="total" value="0" />
                <c:forEach var="item" items="${cart}">
                    <tr>
                        <td>${item.product.name}</td>
                        <td><fmt:formatNumber value="${item.product.price}" type="currency"/></td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.product.price * item.quantity}" type="currency"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/carts" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <button type="submit" class="action-link">Xóa</button>
                            </form>
                        </td>  
                    </tr>
                    <c:set var="total" value="${total + (item.product.price * item.quantity)}" />
                </c:forEach>
                <tr class="total-row">
                    <td colspan="3" style="text-align: right;"><strong>Tổng cộng:</strong></td>
                    <td><fmt:formatNumber value="${total}" type="currency"/></td>
                    <td></td>
                </tr>
            </tbody>
        </table>
        <br>
        <c:if test="${total > 0}">
            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <button type="submit">Tiến hành thanh toán</button>
            </form>
        </c:if>
    </body>
</html>