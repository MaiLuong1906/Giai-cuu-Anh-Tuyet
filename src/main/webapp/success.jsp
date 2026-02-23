<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="vi_VN"/>
<html>
<head>
    <title>Thanh toán thành công</title>
</head>
<body>
    <h2>Thanh toán thành công!</h2>

    <p>
        Tổng tiền phải trả:
        <strong>
            <fmt:formatNumber value="${finalPrice}" type="currency"/>
        </strong>
    </p>

    <br>

    <a href="${pageContext.request.contextPath}/welcome.jsp">
        Quay về trang chủ
    </a>
</body>
</html>
