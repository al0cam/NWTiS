<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Aplikacija 4</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">
			Registracija
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">
			Prijava
		</a></li>
		
		<li><a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika">
			Pregled korisnika
		</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/mvc/server">
			Upravljanje posluziteljom
		</a></li>
	</ul>
</body>
</html>