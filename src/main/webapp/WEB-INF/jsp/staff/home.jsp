<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Home</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-blue-700 mb-6 text-center">Assigned Universities</h1>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mb-8">
        <c:forEach var="uni" items="${universities}">
            <div class="bg-white rounded-lg shadow p-6">
                <h2 class="text-xl font-semibold mb-2">${uni.name}</h2>
                <p class="mb-1"><span class="font-medium">Location:</span> ${uni.location}</p>
                <p class="mb-1"><span class="font-medium">Description:</span> ${uni.description}</p>
            </div>
        </c:forEach>
    </div>
    <div class="bg-white rounded-lg shadow p-6 mb-8">
        <h2 class="text-lg font-semibold mb-4">Application Statistics</h2>
        <div>
            <c:forEach var="entry" items="${stats}">
                <div class="mb-2">
                    <span class="font-medium">${entry.key}:</span> <span>${entry.value}</span>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="flex space-x-4 justify-center">
        <a href="/staff/applications" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Applications</a>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
