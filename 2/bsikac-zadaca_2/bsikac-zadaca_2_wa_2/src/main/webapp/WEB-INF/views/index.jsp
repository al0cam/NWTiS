<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Zadača 2</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledSvihAerodroma">
			Pregled svih aerodroma
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledPracenihAerodroma">
			Pregled pračenih aerodroma
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledAerodroma">
			Pregled jednog aerodroma
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledPolasciAerodroma">
			Pregled polazaka s aerodroma
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregledOdlasciAerodroma">
			Pregled odlazaka s aerodroma
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/dodajAerodroma">
			Dodaj aerodrom
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/problemi/pregledSvihProblema">
			Pregled svih problema
		</a></li>
	</ul>
</body>
</html>