<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register University</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8 flex flex-col items-center justify-center">
    <div class="bg-white rounded-lg shadow p-8 w-full max-w-lg">
        <h1 class="text-3xl font-bold text-blue-700 mb-6 text-center">Register a New University</h1>
        <form action="/facility-admin/register-university" method="post" class="space-y-4">
            <div>
                <label class="block text-gray-700 font-medium mb-1">Name</label>
                <input type="text" name="name" required class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>
            <div>
                <label class="block text-gray-700 font-medium mb-1">Description</label>
                <textarea name="description" required rows="3" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"></textarea>
            </div>
            <div>
                <label class="block text-gray-700 font-medium mb-1">Location</label>
                <input type="text" name="location" required class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>
            <div>
                <label class="block text-gray-700 font-medium mb-1">Image URL</label>
                <input type="text" name="imageUrl" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>
            <div>
                <label class="block text-gray-700 font-medium mb-1">Requirements <span class="text-gray-500 text-sm">(comma separated)</span></label>
                <input type="text" name="requirements" class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition font-semibold">Register University</button>
        </form>
    </div>
</main>
</body>
</html>
