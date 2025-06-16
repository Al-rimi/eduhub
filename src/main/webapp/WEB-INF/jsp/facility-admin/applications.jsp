<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Facility Admin - Applications</title>
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
        <h1 class="text-2xl font-bold text-blue-700 mb-6 text-center">Applications for My University</h1>
        <div class="overflow-x-auto">
            <table class="min-w-full bg-white rounded-lg shadow">
                <thead>
                    <tr class="bg-gray-100 text-gray-700">
                        <th class="px-4 py-2">Student</th>
                        <th class="px-4 py-2">Major</th>
                        <th class="px-4 py-2">Status</th>
                        <th class="px-4 py-2">Last Updated</th>
                        <th class="px-4 py-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="app" items="${applications}">
                        <tr class="border-b hover:bg-gray-50">
                            <td class="px-4 py-2">${app.studentUsername}</td>
                            <td class="px-4 py-2">${app.majorName}</td>
                            <td class="px-4 py-2">${app.status}</td>
                            <td class="px-4 py-2">${app.updatedAt}</td>
                            <td class="px-4 py-2 flex gap-2">
                                <a href="/facility-admin/applications/view?id=${app.id}" class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition text-sm">View</a>
                                <form method="post" action="/facility-admin/applications/status" style="display:inline;">
                                    <input type="hidden" name="appId" value="${app.id}" />
                                    <input type="hidden" name="status" value="ACCEPTED" />
                                    <button type="submit" class="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600 transition text-sm">Approve</button>
                                </form>
                                <form method="post" action="/facility-admin/applications/status" style="display:inline;">
                                    <input type="hidden" name="appId" value="${app.id}" />
                                    <input type="hidden" name="status" value="REJECTED" />
                                    <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition text-sm">Reject</button>
                                </form>
                                <form method="post" action="/facility-admin/applications/delete" style="display:inline;">
                                    <input type="hidden" name="appId" value="${app.id}" />
                                    <button type="submit" class="bg-gray-500 text-white px-2 py-1 rounded hover:bg-gray-600 transition text-sm">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
