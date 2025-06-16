<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - University Details</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <c:if test="${not empty university}">
        <div class="bg-white rounded-lg shadow p-8 max-w-2xl mx-auto">
            <h1 class="text-3xl font-bold text-blue-700 mb-4">${university.name}</h1>
            <p class="text-gray-600 mb-2"><span class="font-medium">Location:</span> ${university.location}</p>
            <c:if test="${not empty university.website}">
                <p class="text-gray-600 mb-2"><span class="font-medium">Website:</span> <a href="${university.website}" target="_blank" class="text-blue-600 underline">${university.website}</a></p>
            </c:if>
            <div class="mb-4">
                <h4 class="text-lg font-semibold text-gray-800 mb-1">Description</h4>
                <p class="text-gray-700">${university.description}</p>
            </div>
            <div class="mb-4">
                <h4 class="text-lg font-semibold text-gray-800 mb-1">Majors Offered</h4>
                <ul class="list-disc list-inside text-gray-700">
                    <c:forEach var="major" items="${university.majors}">
                        <li>${major.name}</li>
                    </c:forEach>
                </ul>
            </div>
            <c:if test="${isStudent}">
                <a href="/application/apply?universityId=${university.id}" class="mt-4 inline-block bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition">Apply Now</a>
            </c:if>
        </div>
    </c:if>
    <c:if test="${empty university}">
        <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center max-w-xl mx-auto">University not found.</div>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
