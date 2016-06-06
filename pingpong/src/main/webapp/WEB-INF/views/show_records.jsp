<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.text.NumberFormat" %>
<html>
<head>
	<title>Show_Records</title>
</head>
<body>
<c:forEach items="${Players}" var="element">
	<h1>${element.firstName} ${element.lastName}</h1>
	<p>Wins: ${element.wins}</p>
	<p>Losses: ${element.losses }</p>
	<p>Points For: ${element.formattedScoreFor }</p>
	<p>Points Against: ${element.formattedScoreAgainst }</p>
</c:forEach>
</body>
</html>
