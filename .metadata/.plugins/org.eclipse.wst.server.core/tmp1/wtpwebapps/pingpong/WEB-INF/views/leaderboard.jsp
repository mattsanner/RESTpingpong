<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.text.NumberFormat" %>
<html>
<head>
	<title>		
		Leaderboard - Sorted By <c:out value="${leaderboard_type}"/>		
	</title>	
</head>
<body>

<h1>		
	Leaderboard - Sorted By <c:out value="${leaderboard_type}"/>		
</h1>

<c:if test="${leaderboard_type eq 'Wins' }">
<c:forEach items="${players}" var="element">
	<h2>${element.firstName} ${element.lastName}</h2>
	<p>Wins: ${element.wins}</p>
	<p>Losses: ${element.losses }</p>
	<p>Points For: ${element.formattedScoreFor }</p>
	<p>Points Against: ${element.formattedScoreAgainst }</p>
</c:forEach>
</c:if>

<c:if test="${leaderboard_type eq 'Points Scored' }">
<c:forEach items="${players}" var="element">
	<h2>${element.firstName} ${element.lastName}</h2>
	<p>Points For: ${element.formattedScoreFor }</p>
	<p>Points Against: ${element.formattedScoreAgainst }</p>
	<p>Wins: ${element.wins}</p>
	<p>Losses: ${element.losses }</p>	
</c:forEach>
</c:if>

</body>
</html>