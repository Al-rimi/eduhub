<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/navbar.jsp" />
<html>
<head>
    <title>Admin - Universities</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold text-blue-700 mb-6 text-center">Universities</h1>
    <div class="overflow-x-auto mb-8">
        <table class="min-w-full bg-white rounded-lg shadow">
            <thead>
                <tr class="bg-gray-100 text-gray-700">
                    <th class="px-4 py-2">Name</th>
                    <th class="px-4 py-2">Location</th>
                    <th class="px-4 py-2">Facility Admin</th>
                    <th class="px-4 py-2">Assign Facility Admin</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="uni" items="${universities}">
                    <tr class="border-b hover:bg-gray-50">
                        <td class="px-4 py-2">${uni.name}</td>
                        <td class="px-4 py-2">${uni.location}</td>
                        <td class="px-4 py-2">
                            <c:choose>
                                <c:when test="${not empty uni.facilityAdmin}">
                                    ${uni.facilityAdmin.username} (${uni.facilityAdmin.email})
                                </c:when>
                                <c:otherwise>
                                    <span class="text-gray-400">None</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="px-4 py-2">
                            <form method="post" action="/admin/universities/assign-facility-admin" class="flex items-center space-x-2">
                                <input type="hidden" name="universityId" value="${uni.id}" />
                                <select name="facilityAdminUserId" class="border rounded px-2 py-1">
                                    <c:forEach var="user" items="${facilityAdmins}">
                                        <option value="${user.id}">${user.username} (${user.email})</option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 transition">Assign</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>
