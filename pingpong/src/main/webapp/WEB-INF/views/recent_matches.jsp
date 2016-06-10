<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.text.NumberFormat" %>
<html>
<head>
	<title>Recent Matches</title>
</head>
<body>
<h1>Recent Matches:</h1>
<c:forEach items="${matches}" var="element">
	<h2>${element.player1} vs. ${element.player2}</h2>
	<p>Score: ${element.p1Score } - ${element.p2Score }</p>
</c:forEach>
</body>
</html>