<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Users</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold text-blue-700 mb-6 text-center">Manage Users</h1>
    <div class="overflow-x-auto">
        <table class="min-w-full bg-white rounded-lg shadow">
            <thead>
                <tr class="bg-gray-100 text-gray-700">
                    <th class="px-4 py-2">Username</th>
                    <th class="px-4 py-2">Email</th>
                    <th class="px-4 py-2">Role</th>
                    <th class="px-4 py-2">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${users}">
                    <tr class="border-b hover:bg-gray-50">
                        <td class="px-4 py-2">${user.username}</td>
                        <td class="px-4 py-2">${user.email}</td>
                        <td class="px-4 py-2">
                            <form method="post" action="/admin/users/role">
                                <input type="hidden" name="userId" value="${user.id}" />
                                <select name="role" class="rounded border border-gray-300 px-2 py-1">
                                    <option value="ROLE_STUDENT" ${user.role == 'ROLE_STUDENT' ? 'selected' : ''}>Student</option>
                                    <option value="ROLE_PLATFORM_ADMIN" ${user.role == 'ROLE_PLATFORM_ADMIN' ? 'selected' : ''}>Admin</option>
                                    <option value="ROLE_STAFF" ${user.role == 'ROLE_STAFF' ? 'selected' : ''}>Staff</option>
                                    <option value="ROLE_FACILITY_ADMIN" ${user.role == 'ROLE_FACILITY_ADMIN' ? 'selected' : ''}>Facility Admin</option>
                                </select>
                                <button type="submit" class="ml-2 bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-600 transition text-sm">Change</button>
                            </form>
                        </td>
                        <td class="px-4 py-2">
                            <form method="post" action="/admin/users/delete" style="display:inline;">
                                <input type="hidden" name="userId" value="${user.id}" />
                                <button type="submit" class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition text-sm">Delete</button>
                            </form>
                        </td>
                        <!-- Optionally, add edit user form/modal here -->
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="mt-6 flex justify-center">
        <a href="/admin/users/add" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Add New User</a>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
