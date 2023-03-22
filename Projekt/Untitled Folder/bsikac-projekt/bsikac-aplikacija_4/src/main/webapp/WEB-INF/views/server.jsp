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
<h1>Upravljanje posluziteljom</h1>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetak">Početna</a><br>
	
      
      <br/>
      <span >Status posluzitelja: ${status}</span><br/>
      <span ${requestScope.rezultatKomande}>Rezultat komande: ${rezultatKomande}</span><br/>
	
		<form action = "${pageContext.servletContext.contextPath}/mvc/server" method = "POST">
      
     		<select name="opcija" id="opcija">
			    <c:forEach items="${map}" var="item">
			        <option value="${item.key}">${item.value}</option>
			    </c:forEach>
		    </select>
         	<input type = "submit" value = "Posalji komandu" />
         </form>
</body>
</html>