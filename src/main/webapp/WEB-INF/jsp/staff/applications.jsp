<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Staff - Applications</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold text-blue-700 mb-6 text-center">Applications for Assigned Universities</h1>
    <div class="overflow-x-auto">
        <table class="min-w-full bg-white rounded-lg shadow">
            <thead>
                <tr class="bg-gray-100 text-gray-700">
                    <th class="px-4 py-2">University</th>
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
                        <td class="px-4 py-2">${app.universityName}</td>
                        <td class="px-4 py-2">${app.studentUsername}</td>
                        <td class="px-4 py-2">${app.majorName}</td>
                        <td class="px-4 py-2">${app.status}</td>
                        <td class="px-4 py-2">${app.updatedAt}</td>
                        <td class="px-4 py-2 flex gap-2">
                            <a href="/staff/applications/view?id=${app.id}" class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition text-sm">View</a>
                            <form method="post" action="/staff/applications/status" style="display:inline;">
                                <input type="hidden" name="appId" value="${app.id}" />
                                <select name="status" class="rounded border border-gray-300 px-2 py-1">
                                    <option value="UNDER_REVIEW">Under Review</option>
                                    <option value="ACCEPTED">Accepted</option>
                                    <option value="REJECTED">Rejected</option>
                                    <option value="WAITLISTED">Waitlisted</option>
                                </select>
                                <button type="submit" class="ml-2 bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition text-sm">Change</button>
                            </form>
                            <form method="post" action="/staff/applications/delete" style="display:inline;">
                                <input type="hidden" name="appId" value="${app.id}" />
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
