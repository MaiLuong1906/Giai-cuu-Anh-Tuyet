<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Product List</title>
</head>
<body>
<h1>Product List</h1>

<p><a href="products?action=create">Create New Product</a></p>

<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Description</th>
        <th>Stock</th>
        <th>Import Date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <c:forEach var="product" items="${productList}">
        <tr>
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.description}</td>
            <td>${product.stock}</td>
            <td>
                <fmt:formatDate value="${product.importDate}" pattern="dd/MM/yyyy"/>
            </td>
            <td>
                <c:choose>
                    <c:when test="${!product.status}">
                        Inactive
                    </c:when>
                    <c:when test="${product.stock == 0}">
                        Out of stock
                    </c:when>
                    <c:otherwise>
                        Available
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="products?action=edit&id=${product.id}">Edit</a> |
                <a href="products?action=delete&id=${product.id}"
                   onclick="return confirm('Are you sure?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
    <c:if test="${totalPages > 1}">
        <div style="margin-top: 15px;">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>[${i}]</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="products?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
                &nbsp;
            </c:forEach>
        </div>
    </c:if>
    <br>
    <a href="welcome.jsp">Back to Welcome</a>
</body>
</html>
