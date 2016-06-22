<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Match Record</title>
</head>
<body>
<h1>${match.player1 }</h1>
<p>Overall Score: ${match.formattedP1Score } </p>
<p>Overall Wins: ${match.p1Wins } </p>
<h1>${match.player2}</h1>
<p>Overall Score: ${match.formattedP2Score }</p>
<p>Overall Wins: ${match.p2Wins } </p>
</body>
</html>
