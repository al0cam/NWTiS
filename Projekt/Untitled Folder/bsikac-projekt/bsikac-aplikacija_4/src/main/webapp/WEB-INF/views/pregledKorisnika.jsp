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
<h1>Pregled korisnika</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Početna</a><br>
	
	<br/>
	<span ${requestScoped.prijavljen}>${prijavljen}</span>
	<br/>
	<br/>
	
	<span>brisanje vlastitog zetona: </span>
	<form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika">
            <input type="submit" name="submit" value="brisanje vlastitog zetona">
     </form>
     
     <br><br>
     <span ${requestScoped.zeton}>${zeton}</span>
     <br><br>
     
     <table>
		<tr>
			<th>Korisnicko ime</th>
			<th>Ime</th>
			<th>Prezime</th>
			<th>Email</th>
			<th>Lozinka</th>
			<c:if  test="${korisnikUGrupiZaBrisanje}">
				<th>Brisanje zetona</th>
			</c:if>
		</tr>
		<c:forEach var="a" items="${requestScope.korisnici}">
			<tr>
				<td>${a.korIme}</td>
				<td>${a.ime}</td>
				<td>${a.prezime}</td>
				<td>${a.email}</td>
				<td>${a.lozinka}</td>
				<c:if test="${korisnikUGrupiZaBrisanje}">
				<td>
					<form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnici/pregledKorisnika/${a.korIme}">
				            <input type="submit" name="submit" value="brisanje zetona">
				     </form>
				</td>
			</c:if>
			</tr>
		</c:forEach>
	</table>
     
     
     
     
     
     
</body>
</html>