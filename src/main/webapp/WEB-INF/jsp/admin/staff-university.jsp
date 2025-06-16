<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/navbar.jsp" />
<!DOCTYPE html>
<html>
<head>
    <title>Staff-University Assignment</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<main class="flex-1 container mx-auto px-4 py-8">
    <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">Assign Universities to Staff</h2>
    <div class="overflow-x-auto">
        <table class="min-w-full bg-white rounded-lg shadow mb-8">
            <thead>
                <tr class="bg-blue-100">
                    <th class="py-2 px-4 text-left">Staff</th>
                    <th class="py-2 px-4 text-left">Assigned Universities</th>
                    <th class="py-2 px-4 text-left">Assign</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="staff" items="${staffList}">
                    <tr class="border-b">
                        <td class="py-2 px-4">${staff.username} <span class="text-xs text-gray-500">(${staff.email})</span></td>
                        <td class="py-2 px-4">
                            <c:forEach var="uni" items="${staff.assignedUniversities}">
                                <form method="post" action="/admin/staff-university/remove" class="inline">
                                    <input type="hidden" name="staffId" value="${staff.id}" />
                                    <input type="hidden" name="universityId" value="${uni.id}" />
                                    <span class="inline-block bg-blue-50 text-blue-700 rounded px-2 py-1 mr-1 mb-1">${uni.name}
                                        <button type="submit" class="ml-1 text-red-500 hover:text-red-700" title="Remove">&times;</button>
                                    </span>
                                </form>
                            </c:forEach>
                        </td>
                        <td class="py-2 px-4">
                            <form method="post" action="/admin/staff-university/assign" class="flex items-center space-x-2">
                                <input type="hidden" name="staffId" value="${staff.id}" />
                                <select name="universityId" class="border rounded px-2 py-1">
                                    <c:forEach var="uni" items="${universities}">
                                        <option value="${uni.id}">${uni.name}</option>
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
