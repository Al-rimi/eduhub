<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Application</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<main class="flex-1 flex items-center justify-center">
<div class="bg-white rounded-lg shadow p-8 w-full max-w-lg">
    <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">Edit Application</h2>
    <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center">${error}</div>
    </c:if>
    <form method="post" action="/student/applications/edit" class="space-y-4">
        <input type="hidden" name="id" value="${application.id}" />
        <c:if test="${not empty applicationDataMap}">
            <c:forEach var="entry" items="${applicationDataMap}">
                <div>
                    <label class="block text-gray-700 mb-1">${entry.key}</label>
                    <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" name="data_${entry.key}" value="${entry.value}" />
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty applicationDataMap}">
            <div>
                <label for="applicationData" class="block text-gray-700 mb-1">Application Data (JSON)</label>
                <textarea class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="applicationData" name="applicationData" rows="10">${application.applicationData}</textarea>
            </div>
        </c:if>
        <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Save Changes</button>
    </form>
</div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
