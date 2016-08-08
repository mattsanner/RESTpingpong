<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.text.NumberFormat" %>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<meta name="description" content="Xpanxion Ping Pong Records">
	<meta name="author" content="Sannman">

	<title>All Match Records</title>
	
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
<body onload="changeRowColor()">

<h2>All Match Records</h2>
<table class="table table-striped">
	<thead>
		<tr>
			<th>Player 1</th>
			<th>Player 2</th>
			<th>Player 1 Wins</th>
			<th>Player 1 Score</th>
			<th>Player 2 Wins</th>
			<th>Player 2 Score</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${matches}" var="element">
			<tr>				
				<td>${element.player1}</td>
				<td>${element.player2}</td>
				<td>${element.p1Wins }</td>
				<td>${element.p1Score }</td>
				<td>${element.p2Wins }</td>
				<td>${element.p2Score }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<p class="lead">
		<a href="/pingpong/" class="btn btn-lg btn-default">Home</a>
</p>

<script>
function changeRowColor() {
	var len = document.getElementsByTagName("tr").length;
	var elems = document.getElementsByTagName("tr");
	var i = 0;
	for(i=0; i<len; i++)
	{
		if(i%2 != 0)
		{
			elems[i].style.backgroundColor = "grey";
		}
	}
}
</script>

</body>
</html>