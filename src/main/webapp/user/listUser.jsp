<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head><title>User List</title></head>
    <body>
        <h1>User Management</h1>
        <a href="logout">Logout</a>

        <c:if test="${not empty message}">
            <p>${message}</p>
        </c:if>

        <p><a href="users?action=create">Add New User</a></p>
        <caption><h2>List of Users</h2></caption>
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>ID</th><th>Username</th><th>Email</th><th>Country</th>
                <th>Role</th><th>Status</th><th>Actions</th>
            </tr>
            <c:forEach var="user" items="${listUser}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.country}</td>
                    <td>${user.role}</td>
                    <td><c:out value="${user.status ? 'Active' : 'Inactive'}"/></td>
                    <td>
                        <a href="users?action=edit&id=${user.id}">Edit</a> |
                        <a href="users?action=delete&id=${user.id}" onclick="return confirm('Delete this user?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <a href="welcome.jsp">Back to welcome</a>
    </body>
</html>
