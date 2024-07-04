<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="helper" class="webapp.webpresentation.CustomersHelper" scope="request"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="resources/css.css" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Customers Info</title>
</head>
<body>
<div class="w3-container w3-black w3-center w3-allerta" id="titulo">
    <p>Client's Info</p>
</div>
<div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
    <br>
    <br>
Number of Clients: <c:out value = "${helper.customers.size()}"></c:out>
<br>
    <br>
    <br>
    <br>
    <table id="clients" class="w3-table w3-bordered">
        <tr class="w3-black">
		<th>Name</th>
		<th>Phone</th>
		<th>Vat</th>
	</tr>
	<c:forEach var="teste" items="${helper.customers}">
		<tr class="w3-blue-grey">
			<td>${teste.designation}</td>
			<td>${teste.phNumber}</td>
			<td>${teste.vat}</td>
		</tr>
		
	</c:forEach>
</table>
<button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>

</div>
</body>
</html>