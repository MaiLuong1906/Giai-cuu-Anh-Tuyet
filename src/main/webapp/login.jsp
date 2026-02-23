<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head><title>Login</title></head>
    <body>

    <center>
        <h2>Login</h2>

        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>

        <form action="login" method="post">
            <table border="0">
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" value="${not empty rememberedUsername ? rememberedUsername : ''}" required></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" value="${not empty rememberedPassword ? rememberedPassword : ''}" required></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="checkbox" name="remember" value="true"
                               <c:if test="${not empty rememberedUsername}">checked</c:if>> Remember me
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <button type="submit">Login</button>
                    </td>
                </tr>
            </table>
        </form>
    </center>

</body>
</html>
