<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Product</title>
</head>
<body>

<h1>Edit Product</h1>
<a href="products">Cancel</a>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<form action="products?action=edit" method="post">
    <!-- ID ẩn -->
    <input type="hidden" name="id" value="${product.id}">

    <table>
        <tr>
            <td>Name:</td>
            <td>
                <input type="text"
                       name="name"
                       value="${product.name}"
                       required>
            </td>
        </tr>

        <tr>
            <td>Price:</td>
            <td>
                <input type="number"
                       name="price"
                       step="0.01"
                       min="0"
                       value="${product.price}"
                       required>
            </td>
        </tr>

        <tr>
            <td>Description:</td>
            <td>
                <textarea name="description" rows="3" cols="30">${product.description}</textarea>
            </td>
        </tr>

        <tr>
            <td>Stock:</td>
            <td>
                <input type="number"
                       name="stock"
                       min="0"
                       value="${product.stock}"
                       required>
            </td>
        </tr>

        <tr>
            <td>Import Date:</td>
            <td>
                <input type="date"
                       name="import_date"
                       value="<fmt:formatDate value='${product.importDate}' pattern='yyyy-MM-dd'/>"
                       required>
            </td>
        </tr>

        <tr>
            <td>Status:</td>
            <td>
                <input type="checkbox"
                       name="status"
                       value="true"
                       <c:if test="${product.status}">checked</c:if>>
                Active
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <button type="submit">Save</button>
                <button type="reset">Reset</button>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
