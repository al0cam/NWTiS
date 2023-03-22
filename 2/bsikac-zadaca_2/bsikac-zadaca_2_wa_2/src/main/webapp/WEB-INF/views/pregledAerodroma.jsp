<%@page import="org.foi.nwtis.bsikac.zadaca_2.mvc.AerodromiKlijent"%>
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
<h1>Pregled aerodroma</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Početna</a><br>
	
	<div ${requestScope.aerodromi}>
		ICAO: ${a.icao}<br>
		Naziv: ${a.naziv} <br>
		Drzava: ${a.drzava} <br>
	</div>
	
	<form method="POST" action="${pageContext.servletContext.contextPath}/mvc/pregledAerodroma">
            <label for="icao">Icao: </label>
            <input type="text" name="icao">
            <input type="submit" name="submit" value="submit">
     </form>
</body>
</html>