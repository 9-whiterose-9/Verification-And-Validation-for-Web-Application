<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="saleDeliveryHelper" class="webapp.webpresentation.SalesDeliveryHelper" scope="request"/>
<jsp:useBean id="addressesHelper" class="webapp.webpresentation.AddressesHelper" scope="request"/>
<jsp:useBean id="salesHelper" class="webapp.webpresentation.SalesHelper" scope="request"/>
<HTML>
<HEAD>
<link rel="stylesheet" href="resources/css.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<TITLE>Enter Name</TITLE>
</HEAD>
<BODY>
<div class="w3-container w3-black w3-center w3-allerta" id="titulo">
    <p>Add SaleDelivery</p>
    </div>
<div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
<br>
<FORM ACTION="AddSaleDeliveryPageController" METHOD="POST">
<P>Please enter address id:</P>
<br>
<INPUT class="w3-btn w3-white w3-border w3-round-large" TYPE="TEXT" NAME="addr_id">
<br>
<br>
<c:if test = "${addressesHelper.getAddresses().size() > 0}">
    <table class="w3-table w3-bordered">
	   <tr class="w3-black">
		<th>Id</th>
		<th>Address</th>
		<th>Door</th>
		<th>Postal Code</th>
		<th>Locality</th>
	</tr>
	<c:forEach var="addr" items="${addressesHelper.getAddresses()}">
       <tr class="w3-blue-grey">
       		<td>${addr.getId()}</td>
			<td>${addr.getAddress().split(";")[0]}</td>
			<td>${addr.getAddress().split(";")[1]}</td>
			<td>${addr.getAddress().split(";")[2]}</td>
			<td>${addr.getAddress().split(";")[3]}</td>
		</tr>	
	</c:forEach>
</table>
</c:if>
<BR>
<BR>
<P>Please enter sale's id:</P>
<INPUT class="w3-btn w3-white w3-border w3-round-large" TYPE="TEXT" NAME="sale_id">
<br>
<br>
<INPUT class="w3-btn w3-black w3-border w3-round-large" TYPE="SUBMIT" VALUE="Insert">
</FORM>
<br>
<br>
<c:if test = "${salesHelper.getSales().size() > 0}">
    <table class="w3-table w3-bordered">
	   <tr class="w3-black">
		<th>Id</th>
		<th>Date</th>
		<th>Total</th>
		<th>Status</th>
		<th>Customer Vat Number</th>
	</tr>
	<c:forEach var="sales" items="${salesHelper.getSales()}">
       <tr class="w3-blue-grey">
			<td>${sales.id}</td>
			<td>${sales.getDate()}</td>
			<td>${sales.getTotal()}</td>
			<td>${sales.getStatus()}</td>
			<td>${sales.getCustomerVat()}</td>
		</tr>	
	</c:forEach>
</table>
</c:if>
<button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>
</div>
</BODY>
</HTML>