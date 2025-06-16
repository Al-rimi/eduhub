<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - My Applications</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/common/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">My Applications</h2>
    <c:if test="${empty applications}">
        <div class="bg-blue-100 text-blue-700 px-4 py-2 rounded mb-4 text-center">You have not submitted any applications yet.</div>
    </c:if>
    <c:if test="${not empty applications}">
        <div class="overflow-x-auto">
        <table class="min-w-full bg-white rounded-lg shadow">
            <thead>
                <tr class="bg-gray-100 text-gray-700">
                    <th class="px-4 py-2">University</th>
                    <th class="px-4 py-2">Major</th>
                    <th class="px-4 py-2">Status</th>
                    <th class="px-4 py-2">Last Updated</th>
                    <th class="px-4 py-2">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="app" items="${applications}">
                    <tr class="border-b hover:bg-gray-50">
                        <td class="px-4 py-2">${app.universityName}</td>
                        <td class="px-4 py-2">${app.majorName}</td>
                        <td class="px-4 py-2">${app.status}</td>
                        <td class="px-4 py-2">${app.updatedAt}</td>
                        <td class="px-4 py-2"><a href="/application/detail?id=${app.id}" class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 transition text-sm">View</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </div>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</body>
</html>
