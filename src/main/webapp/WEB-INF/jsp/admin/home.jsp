<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub Admin Home</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-blue-700 mb-6 text-center">Admin Dashboard</h1>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
            <h2 class="text-lg font-semibold mb-2">Total Users</h2>
            <div class="text-2xl font-bold text-blue-600">${totalUsers}</div>
        </div>
        <div class="bg-white rounded-lg shadow p-6">
            <h2 class="text-lg font-semibold mb-2">Total Applications</h2>
            <div class="text-2xl font-bold text-blue-600">${totalApplications}</div>
        </div>
        <div class="bg-white rounded-lg shadow p-6">
            <h2 class="text-lg font-semibold mb-2">Top University</h2>
            <div class="text-2xl font-bold text-blue-600">${topUniversity}</div>
        </div>
    </div>
    <div class="bg-white rounded-lg shadow p-6 mb-8">
        <h2 class="text-lg font-semibold mb-4">Applications by University</h2>
        <div id="chart-applications-by-university" class="w-full h-64"></div>
        <!-- Chart.js or similar can be used here -->
    </div>
    <div class="flex space-x-4 justify-center">
        <a href="/admin/users" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Users</a>
        <a href="/admin/applications" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Applications</a>
        <a href="/admin/staff-university" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Staff-University Assignment</a>
    </div>
    <!-- More dashboard features can be added here -->
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
