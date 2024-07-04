<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="helper" class="webapp.webpresentation.CustomerHelper" scope="request"/>
<jsp:useBean id="addressesHelper" class="webapp.webpresentation.AddressesHelper" scope="request"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="resources/css.css" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Customer Info</title>
</head>
<body>
<div class="w3-container w3-black w3-center w3-allerta" id="titulo">
    <p>Client Info</p>
</div>
<div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
<form>
<p>Identifier: <c:out value ="${helper.id}"></c:out></p>
<p>Name: <c:out value ="${helper.designation}"></c:out></p>
<p>Contact Info: <c:out value ="${helper.phNumber}"></c:out></p>
</form>
<br>
<br>
<c:if test = "${addressesHelper.getAddresses().size() > 0}">
	<table class="w3-table w3-bordered">
	<tr class="w3-black">
		<th>Address</th>
		<th>Door</th>
		<th>Postal Code</th>
		<th>Locality</th>
	</tr>
	<c:forEach var="addr" items="${addressesHelper.getAddresses()}">
		<tr class="w3-black">
			<td>${addr.getAddress().split(";")[0]}</td>
			<td>${addr.getAddress().split(";")[1]}</td>
			<td>${addr.getAddress().split(";")[2]}</td>
			<td>${addr.getAddress().split(";")[3]}</td>
		</tr>	
	</c:forEach>
</table>
</c:if>
</div>
<button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>
</body>
</html>
