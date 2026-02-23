<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <center>
        <h2>Welcome, ${user.username}!</h2>
        <p>You have successfully logged in.</p>
        
        <h3>Menu:</h3>
        <p>
            <a href="${pageContext.request.contextPath}/users">List Users</a> |
            <a href="${pageContext.request.contextPath}/products">List Products</a> |
            <a href="${pageContext.request.contextPath}/cart/productListCart.jsp">Product List Cart</a>
        </p>
        
        <p><a href="logout">Logout</a></p>
    </center>
</body>
</html>