<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="saleDeliveryHelper" class="webapp.webpresentation.SalesDeliveryHelper" scope="request"/>
<jsp:useBean id="addressesHelper" class="webapp.webpresentation.AddressesHelper" scope="request"/>
<jsp:useBean id="salesHelper" class="webapp.webpresentation.SalesHelper" scope="request"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="resources/css.css" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Sales Info</title>
</head>
<body>
<div class="w3-container w3-black w3-center w3-allerta" id="titulo">
<P>Sale Info</P>
</div>
<br>
<div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
<c:if test = "${saleDeliveryHelper.getSalesDelivery().size() > 0}">
	<table class="w3-table w3-bordered">
	   <tr class="w3-black">
			<th>Id</th>
			<th>Sale Id</th>
			<th>Address id</th>
		</tr>
		<c:forEach var="sales" items="${saleDeliveryHelper.getSalesDelivery()}">
			
			
			<tr class="w3-blue-grey">
				<td>${sales.id}</td>
				<td>${sales.getSale_id()}</td>
				<td>${sales.getAddr_id()}</td>
			</tr>	
		</c:forEach>
	</table>
	
</c:if>
</div>
<button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>
</body>
</html>