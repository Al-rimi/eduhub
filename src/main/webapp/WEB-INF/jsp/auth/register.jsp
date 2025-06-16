<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - Register</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow p-8 w-full max-w-md">
        <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">Register</h2>
        <c:if test="${not empty error}">
            <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center">${error}</div>
        </c:if>
        <form method="post" action="/register" class="space-y-4">
            <div>
                <label for="username" class="block text-gray-700 mb-1">Username</label>
                <input type="text" id="username" name="username" autocomplete="username" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required />
            </div>
            <div>
                <label for="email" class="block text-gray-700 mb-1">Email</label>
                <input type="email" id="email" name="email" autocomplete="email" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required />
            </div>
            <div>
                <label for="password" class="block text-gray-700 mb-1">Password</label>
                <input type="password" id="password" name="password" autocomplete="new-password" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required />
            </div>
            <div>
                <label for="role" class="block text-gray-700 mb-1">Role</label>
                <select id="role" name="role" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" required>
                    <option value="STUDENT">Student</option>
                    <option value="PLATFORM_ADMIN">Platform Admin</option>
                    <option value="STAFF">Staff</option>
                    <option value="FACILITY_ADMIN">Facility Admin</option>
                </select>
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Register</button>
        </form>
        <div class="mt-4 text-center">
            <a href="/login" class="text-blue-600 hover:underline">Already have an account? Login</a>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
