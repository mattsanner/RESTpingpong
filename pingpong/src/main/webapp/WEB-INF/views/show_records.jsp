<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<meta name="description" content="Xpanxion Ping Pong Records">
	<meta name="author" content="Sannman">
	
	<title>Match Record</title>
	
	<!-- Bootstrap core CSS -->
	<!-- ../../../resource/bootstrap/dist/css/bootstrap.min.css -->
   	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
   		integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
   		crossorigin="anonymous">
   		
   	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" 
	integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

	<!-- Latest compiled and minified JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" 
	integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
	
	<link href=<c:url value="/resources/cover.css" /> rel="stylesheet">
</head>
<body>
<h1>${match.player1 }</h1>
<p>Overall Score: ${match.formattedP1Score } </p>
<p>Overall Wins: ${match.p1Wins } </p>
<h1>${match.player2}</h1>
<p>Overall Score: ${match.formattedP2Score }</p>
<p>Overall Wins: ${match.p2Wins } </p>

<p class="lead">
		<a href="/pingpong/" class="btn btn-lg btn-default">Home</a>
</p>
</body>
</html>
