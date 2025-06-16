<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Applications</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold text-blue-700 mb-6 text-center">My Applications</h1>
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
                        <td class="px-4 py-2 flex gap-2">
                            <a href="/application/detail?id=${app.id}" class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition text-sm">View</a>
                            <a href="/student/applications/edit?id=${app.id}" class="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600 transition text-sm">Edit</a>
                            <form method="post" action="/student/applications/delete" style="display:inline;">
                                <input type="hidden" name="applicationId" value="${app.id}" />
                                <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition text-sm">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
