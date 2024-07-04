<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="helpers" class="webapp.webpresentation.CustomersHelper" scope="request"/>
<html>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="resources/css.css" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Allerta+Stencil">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<TITLE>Enter VatNumber</TITLE>
</HEAD>
<BODY>
    <div class="w3-container w3-black w3-center w3-allerta" id="titulo">
    <p>Remove Customer</p>
    </div>
    <div class="w3-container w3-blue-grey w3-center w3-allerta" id="body">
        <FORM ACTION="RemoveCustomerPageController" METHOD="POST">
            <br>
            <P>Please enter customer's vat number:</P>
            <INPUT class="w3-btn w3-white w3-border w3-round-large" TYPE="TEXT" NAME="vat"> 
            <INPUT class="w3-button w3-black w3-round-large w3-allerta"  TYPE="SUBMIT" VALUE="Remove" id="botao1" name="submit">
        </FORM>
<br>
<br>
<c:if test = "${helpers.customers.size() > 0}">
        <table class="w3-table w3-bordered">
        <tr class="w3-black">
		<th>Name</th>
		<th>Phone</th>
		<th>Vat</th>
	</tr>
	<c:forEach var="teste" items="${helpers.customers}">
        <tr class="w3-blue-grey">
			<td>${teste.designation}</td>
			<td>${teste.phNumber}</td>
			<td>${teste.vat}</td>
		</tr>	
	</c:forEach>
</table>
</c:if>
<button class="w3-button w3-white w3-round-large w3-allerta" id="botao_home" onclick="window.location.href='index.html'">Home</button>
    </div>
</BODY>
</HTML>