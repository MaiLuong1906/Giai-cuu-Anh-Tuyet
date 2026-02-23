

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>Create Product</title>
    </head>
    <body>
        <h1>Create New Product</h1>
        <a href="products">Cancel</a>

        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

        <form action="products?action=create" method="post">
            <table>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" name="name" required></td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td><input type="number" step="0.01" name="price" min="0" required></td>
                </tr>
                <tr>
                    <td>Description:</td>
                    <td><input type="text" name="description"></td>
                </tr>
                <tr>
                    <td>Stock:</td>
                    <td><input type="number" name="stock" min="0" required></td>
                </tr>
                <tr>
                    <td>Import Date:</td>
                    <td><input type="date" name="import_date" required></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="submit">Create</button>
                        <button type="reset">Reset</button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>

