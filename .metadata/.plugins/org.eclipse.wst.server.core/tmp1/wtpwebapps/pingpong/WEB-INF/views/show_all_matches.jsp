<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.text.NumberFormat" %>
<html>
<head>
	<title>All Match Records</title>
</head>
<body>
<h1>All Match Records</h1>
<c:forEach items="${matches}" var="element">
	<h2>${element.player1} vs. ${element.player2}</h2>
	<p>${element.player1}'s score: ${element.p1Score }</p>
	<p>${element.p2Score}'s score: ${element.p2Score }</p>
	<p>${element.player1}'s wins: ${element.p1Wins }</p>
	<p>${element.player2}'s wins: ${element.p2Wins }</p>
</c:forEach>
</body>
</html>