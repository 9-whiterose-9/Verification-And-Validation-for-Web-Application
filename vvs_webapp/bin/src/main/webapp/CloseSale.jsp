<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="salesHelper" class="webapp.webpresentation.SalesHelper" scope="request"/>
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="resources/app.css" />
<link rel="stylesheet" href="resource/css.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<TITLE>Enter Sale Id</TITLE>
</HEAD>
<BODY>
<div class="w3-container w3-black w3-center w3-allerta" id="titulo">
        <P>Close Sale</P>
    </div>
    <div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
<FORM ACTION="UpdateSaleStatusPageControler" METHOD="POST">
<P>Please enter sale's id:</P>
                <INPUT  class="w3-btn w3-white w3-border w3-round-large" TYPE="TEXT" NAME="id"> <INPUT  class="w3-button w3-black w3-round-large w3-allerta" TYPE="SUBMIT" VALUE="Close Sale" id="botao1">
                <br>
                <br>
                <br>
                <br>
</FORM>
<c:if test = "${salesHelper.getSales().size() > 0}">
	<table class="w3-table w3-bordered">
	<tr class="w3-black">
		<th>Id</th>
		<th>Date</th>
		<th>Total</th>
		<th>Status</th>
		<th>Customer Vat Number</th>
	<tr class="w3-blue-grey">
	<c:forEach var="sales" items="${salesHelper.getSales()}">
		<tr>
			<td>${sales.id}</td>
			<td>${sales.getDate()}</td>
			<td>${sales.getTotal()}</td>
			<td>${sales.getStatus()}</td>
			<td>${sales.getCustomerVat()}</td>
		</tr>	
	</c:forEach>
</table>
</c:if>
        </div>
            <button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>
</BODY>
</HTML>