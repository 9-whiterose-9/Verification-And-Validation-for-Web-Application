<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="helper" class="webapp.webpresentation.CustomerHelper" scope="request"/>
<jsp:useBean id="helpers" class="webapp.webpresentation.CustomersHelper" scope="request"/>
<jsp:useBean id="saleHelper" class="webapp.webpresentation.SaleHelper" scope="request"/>
<jsp:useBean id="salesHelper" class="webapp.webpresentation.SalesHelper" scope="request"/>
<jsp:useBean id="addressesHelper" class="webapp.webpresentation.AddressesHelper" scope="request"/>
<jsp:useBean id="salesDeliveryHelper" class="webapp.webpresentation.SalesDeliveryHelper" scope="request"/>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer Info</title>
</head>
<body>
<body>
	<p>Error Messages: </p>
	<ul>
	<c:forEach var="message" items="${helpers.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
	<ul>
	<c:forEach var="message" items="${helper.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
	<ul>
	<c:forEach var="message" items="${saleHelper.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
	<ul>
	<c:forEach var="message" items="${salesHelper.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
	<ul>
	<c:forEach var="message" items="${addressesHelper.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
	<ul>
	<c:forEach var="message" items="${salesDeliveryHelper.messages}">
		<li>${message} 
	</c:forEach>
	</ul>
</body>
</html>
