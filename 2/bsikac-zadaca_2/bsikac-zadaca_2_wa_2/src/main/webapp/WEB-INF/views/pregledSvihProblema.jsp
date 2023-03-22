<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Pregled svih problema</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Početna</a><br>
	<table>
		<tr>
			<th>ICAO</th>
			<th>Opis</th>
		</tr>
		<c:forEach var="a" items="${requestScope.problemi}">
			<tr>
				<td>${a.ident}</td>
				<td>${a.description}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>