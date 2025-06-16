<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>EduHub - Application Form</title>
    <link rel="stylesheet" href="/static/css/tailwind.min.css"/>
</head>
<body class="bg-gray-50 min-h-screen flex flex-col">
<jsp:include page="/WEB-INF/jsp/common/navbar.jsp"/>
<main class="flex-1 flex items-center justify-center">
<div class="bg-white rounded-lg shadow p-8 w-full max-w-lg">
    <h2 class="text-2xl font-bold text-blue-700 mb-6 text-center">Application Form</h2>
    <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 px-4 py-2 rounded mb-4 text-center">${error}</div>
    </c:if>
    <form method="post" action="/application/apply" class="space-y-4">
        <input type="hidden" name="universityId" value="${university.id}" />
        <div>
            <label for="majorId" class="block text-gray-700 mb-1">Major</label>
            <select class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="majorId" name="majorId" required>
                <c:forEach var="major" items="${university.majors}">
                    <option value="${major.id}" <c:if test="${major.id == selectedMajorId}">selected</c:if>>${major.name}</option>
                </c:forEach>
            </select>
        </div>
        <div>
            <label for="fullName" class="block text-gray-700 mb-1">Full Name</label>
            <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="fullName" name="fullName" value="${form.fullName}" required />
        </div>
        <div>
            <label for="dateOfBirth" class="block text-gray-700 mb-1">Date of Birth</label>
            <input type="date" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="dateOfBirth" name="dateOfBirth" value="${form.dateOfBirth}" required />
        </div>
        <div>
            <label for="nationality" class="block text-gray-700 mb-1">Nationality</label>
            <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="nationality" name="nationality" value="${form.nationality}" required />
        </div>
        <div>
            <label for="highSchoolName" class="block text-gray-700 mb-1">High School Name</label>
            <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="highSchoolName" name="highSchoolName" value="${form.highSchoolName}" required />
        </div>
        <div>
            <label for="graduationYear" class="block text-gray-700 mb-1">Graduation Year</label>
            <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="graduationYear" name="graduationYear" value="${form.graduationYear}" required />
        </div>
        <div>
            <label for="gpa" class="block text-gray-700 mb-1">GPA</label>
            <input type="text" class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="gpa" name="gpa" value="${form.gpa}" required />
        </div>
        <div>
            <label for="essay" class="block text-gray-700 mb-1">Essay</label>
            <textarea class="w-full rounded border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400" id="essay" name="essay" rows="4">${form.essay}</textarea>
        </div>
        <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">Submit Application</button>
    </form>
</div>
</main>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</body>
</html>
