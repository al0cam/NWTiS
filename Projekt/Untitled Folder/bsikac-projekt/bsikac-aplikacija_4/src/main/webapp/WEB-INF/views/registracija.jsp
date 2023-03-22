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
<h1>Registracija</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Početna</a><br>
	
      <form action = "${pageContext.servletContext.contextPath}/mvc/korisnici/registracija" method = "POST">
         Korisnicko ime: <input type = "text" name = "korIme" />
         <br />
         Ime: <input type = "text" name = "ime">
         <br />
         Prezime: <input type = "text" name = "prezime" />
         <br />
         Email: <input type = "text" name = "email" />
         <br />
         Lozinka: <input type = "text" name = "lozinka" />
         <br />
         <input type = "submit" value = "Submit" />
      </form>
         <br />
      
      <span ${requestScope.reg}>${reg}</span>
      
      
      
</body>
</html>