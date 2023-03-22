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
<h1>Pregled polazaka na dan</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">PoÄŤetna</a><br>
	<table>
		<tr>
			<th>ICAO24</th>
			<th>FirstSeen</th>
			<th>EST departure airport</th>
			<th>LastSeen</th>
			<th>EST arrival airport</th>
			<th>Callsign</th>
			<th>estArrivalAirportHorizDistance</th>
			<th>estArrivalAirportVertDistance</th>
			<th>estDepartureAirportHorizDistance</th>
			<th>estDepartureAirportVertDistance</th>
			<th>arrivalAirportCandidatesCount</th>
			<th>departureAirportCandidatesCount</th>
		</tr>
		<c:forEach var="a" items="${requestScope.aerodromi}">
			<tr>
				<td>${a.icao}</td>
				<td>${a.icao24}</td>
				<td>${a.firstSeen}</td>
				<td>${a.estDepartureAirport}</td>
				<td>${a.lastSeen}</td>
				<td>${a.estArrivalAirport}</td>
				<td>${a.callsign}</td>
				<td>${a.estArrivalAirportdorizDistance}</td>
				<td>${a.estArrivalAirportVertDistance}</td>
				<td>${a.estDepartureAirportdorizDistance}</td>
				<td>${a.estDepartureAirportVertDistance}</td>
				<td>${a.arrivalAirportCandidatesCount}</td>
				<td>${a.departureAirportCandidatesCount}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>