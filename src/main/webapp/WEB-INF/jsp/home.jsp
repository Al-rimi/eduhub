<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - Home</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold text-blue-700 mb-6 text-center">Universities & Educational Facilities</h1>
    <form method="get" action="/home" class="max-w-xl mx-auto flex gap-2 mb-8">
        <input type="text" name="name" class="flex-1 rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" placeholder="Search by name" value="${param.name}"/>
        <button type="submit" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Search</button>
    </form>
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        <c:forEach var="uni" items="${universities}">
            <div class="bg-white rounded-lg shadow hover:shadow-lg transition p-6 flex flex-col justify-between">
                <div>
                    <h2 class="text-xl font-semibold text-gray-800 mb-2">${uni.name}</h2>
                    <p class="text-gray-500 mb-1"><span class="font-medium">Location:</span> ${uni.location}</p>
                    <c:if test="${not empty uni.website}">
                        <p class="text-gray-500 mb-1"><span class="font-medium">Website:</span> <a href="${uni.website}" target="_blank" class="text-blue-600 underline">${uni.website}</a></p>
                    </c:if>
                </div>
                <a href="/universities/${uni.id}" class="mt-4 inline-block bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition text-center">View Details</a>
            </div>
        </c:forEach>
    </div>
    <c:if test="${hasMore}">
        <form method="get" action="/home" class="flex justify-center mt-8">
            <input type="hidden" name="page" value="${nextPage}"/>
            <button type="submit" class="bg-gray-200 text-gray-700 px-6 py-2 rounded hover:bg-gray-300 transition">Show More</button>
        </form>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
