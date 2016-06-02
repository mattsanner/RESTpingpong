<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Match Players</title>
</head>
<body>
<c:forEach items="${Match}" var="element">
	<h1>${element.firstName} ${element.lastName}</h1>
	<p>Wins: ${element.wins}</p>
	<p>Losses: ${element.losses }</p>
	<p>Points For: ${element.scoreFor }</p>
	<p>Points Against: ${element.scoreAgainst }</p>
</c:forEach>
</body>
</html>