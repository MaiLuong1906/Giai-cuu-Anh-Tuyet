<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head><title>Edit User</title></head>
    <body>
        <h1>Edit User</h1>
        <a href="users">Cancel</a>

        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

        <form action="users?action=edit" method="post">
            <input type="hidden" name="id" value="${user.id}">
            <table>
                <tr><td>Username:</td><td><input type="text" name="username" value="${user.username}" required></td></tr>
                <tr><td>Email:</td><td><input type="text" name="email" value="${user.email}" required></td></tr>
                <tr><td>Password:</td><td><input type="password" name="password" value="${user.password}" required></td></tr>
                <tr><td>Country:</td><td><input type="text" name="country" value="${user.country}"></td></tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <select name="role" required>
                            <option value="user" <c:if test="${user.role == 'user'}">selected</c:if>>User</option>
                            <option value="admin" <c:if test="${user.role == 'admin'}">selected</c:if>>Admin</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Status:</td>
                    <td>
                        <select name="status" required>
                            <option value="true" <c:if test="${user.status}">selected</c:if>>Active</option>
                            <option value="false" <c:if test="${!user.status}">selected</c:if>>Inactive</option>
                        </select>
                    </td>
                </tr>
                <tr><td>Birth Date:</td><td><input type="date" name="dob" value="${user.dob}" required></td></tr>
                <tr><td colspan="2">
                        <button type="submit">Save</button>
                        <button type="reset">Reset</button>
                    </td></tr>
            </table>
        </form>
    </body>
</html>
