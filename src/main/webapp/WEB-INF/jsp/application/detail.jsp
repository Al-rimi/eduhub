<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - Application Detail</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/common/navbar.jsp"/>
<main class="flex-1 container mx-auto px-4 py-8">
    <c:if test="${not empty application}">
        <div class="bg-white rounded-lg shadow p-8 max-w-2xl mx-auto">
            <h2 class="text-2xl font-bold text-blue-700 mb-4">Application for ${application.universityName}</h2>
            <p class="text-gray-700 mb-2"><span class="font-medium">Major:</span> ${application.majorName}</p>
            <p class="text-gray-700 mb-2"><span class="font-medium">Status:</span> ${application.status}</p>
            <p class="text-gray-700 mb-2"><span class="font-medium">Submitted:</span> ${application.createdAt}</p>
            <p class="text-gray-700 mb-2"><span class="font-medium">Last Updated:</span> ${application.updatedAt}</p>
            <c:if test="${not empty applicationDataMap}">
                <h4 class="text-lg font-semibold text-gray-800 mb-1 mt-4">Application Details</h4>
                <c:forEach var="entry" items="${applicationDataMap}">
                    <p class="text-gray-700 mb-1"><span class="font-medium">${entry.key}:</span> ${entry.value}</p>
                </c:forEach>
            </c:if>
            <c:if test="${empty applicationDataMap}">
                <pre class="bg-gray-100 rounded p-4 text-sm text-gray-800">${application.applicationData}</pre>
            </c:if>
        </div>
    </c:if>
    <c:if test="${empty application}">
        <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center max-w-xl mx-auto">Application not found.</div>
    </c:if>
</main>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</body>
</html>
