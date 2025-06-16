<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Facility Admin Home</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6 text-center">
            ${error}
        </div>
    </c:if>
    <c:if test="${empty error}">
        <h1 class="text-3xl font-bold text-blue-700 mb-6 text-center">My University</h1>
        <div class="bg-white rounded-lg shadow p-6 mb-8">
            <h2 class="text-xl font-semibold mb-2">${university.name}</h2>
            <p class="mb-1"><span class="font-medium">Location:</span> ${university.location}</p>
            <p class="mb-1"><span class="font-medium">Description:</span> ${university.description}</p>
            <c:if test="${not empty university.imageUrl}">
                <img src="${university.imageUrl}" alt="University Image" class="w-48 h-32 object-cover rounded mt-2"/>
            </c:if>
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
            <a href="/facility-admin/applications" class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition">Applications</a>
            <a href="/facility-admin/edit" class="bg-yellow-600 text-white px-6 py-2 rounded hover:bg-yellow-700 transition">Edit University</a>
        </div>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
