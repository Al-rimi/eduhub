<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="bg-white border-b border-gray-200 px-4 py-2 flex items-center justify-between">
  <a href="/" class="text-2xl font-bold text-blue-600">EduHub</a>
  <div class="flex items-center space-x-4">
    <sec:authorize access="hasRole('PLATFORM_ADMIN')">
      <a href="/admin/home" class="text-gray-700 hover:text-blue-600 transition">Home</a>
      <a href="/admin/users" class="text-gray-700 hover:text-blue-600 transition">Users</a>
      <a href="/admin/applications" class="text-gray-700 hover:text-blue-600 transition">Applications</a>
      <a href="/admin/staff-university" class="text-gray-700 hover:text-blue-600 transition">Staff</a>
    </sec:authorize>
    <sec:authorize access="hasRole('STUDENT')">
      <a href="/student/home" class="text-gray-700 hover:text-blue-600 transition">Home</a>
      <a href="/student/applications" class="text-gray-700 hover:text-blue-600 transition">My Applications</a>
    </sec:authorize>
    <sec:authorize access="hasRole('STAFF')">
      <a href="/staff/home" class="text-gray-700 hover:text-blue-600 transition">Home</a>
      <a href="/staff/applications" class="text-gray-700 hover:text-blue-600 transition">Applications</a>
    </sec:authorize>
    <sec:authorize access="hasRole('FACILITY_ADMIN')">
      <a href="/facility-admin/home" class="text-gray-700 hover:text-blue-600 transition">Home</a>
      <a href="/facility-admin/applications" class="text-gray-700 hover:text-blue-600 transition">Applications</a>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
      <span class="text-gray-700">Welcome, <sec:authentication property="name"/>!</span>
      <a href="/logout" class="text-gray-700 hover:text-blue-600 transition">Logout</a>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
      <a href="/login" class="text-gray-700 hover:text-blue-600 transition">Login</a>
      <a href="/register" class="text-gray-700 hover:text-blue-600 transition">Register</a>
    </sec:authorize>
  </div>
</nav>
