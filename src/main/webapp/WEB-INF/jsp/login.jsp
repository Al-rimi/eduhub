<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - Login</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="navbar.jsp"/>
<main class="flex-1 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow p-8 w-full max-w-md">
        <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">Login</h2>
        <c:if test="${not empty error}">
            <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center">${error}</div>
        </c:if>
        <form method="post" action="/login" class="space-y-4">
            <div>
                <label for="username" class="block text-gray-700 mb-1">Username</label>
                <input type="text" id="username" name="username" autocomplete="username" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required />
            </div>
            <div>
                <label for="password" class="block text-gray-700 mb-1">Password</label>
                <input type="password" id="password" name="password" autocomplete="current-password" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required />
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Login</button>
        </form>
        <div class="mt-4 text-center">
            <a href="/register" class="text-blue-600 hover:underline">Don't have an account? Register</a>
        </div>
    </div>
</main>
<jsp:include page="footer.jsp"/>
</body>
</html>
