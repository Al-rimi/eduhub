<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error - Facility Admin</title>
</head>
<body>
    <h1>Error</h1>
    <div style="color:red;">
        <c:out value="${error}" />
    </div>
    <a href="/logout">Logout</a>
</body>
</html>
