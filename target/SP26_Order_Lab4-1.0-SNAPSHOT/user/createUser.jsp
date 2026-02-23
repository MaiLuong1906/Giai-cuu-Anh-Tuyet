<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head><title>Create User</title></head>
    <body>
        <h1>Create New User</h1>
        <a href="users">Cancel</a>

        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

        <form action="users?action=create" method="post">
            <table>
                <tr><td>Username:</td><td><input type="text" name="username" required></td></tr>
                <tr><td>Email:</td><td><input type="text" name="email" required></td></tr>
                <tr><td>Password:</td><td><input type="password" name="password" required></td></tr>
                <tr><td>Country:</td><td><input type="text" name="country"></td></tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <select name="role" required>
                            <option value="User">User</option>
                            <option value="Admin">Admin</option>
                        </select>
                    </td>
                </tr>
                <tr><td>Birth Date:</td><td><input type="date" name="dob" required></td></tr>
                <tr><td colspan="2">
                        <button type="submit">Create</button>
                        <button type="reset">Reset</button>
                    </td></tr>
            </table>
        </form>
    </body>
</html>
