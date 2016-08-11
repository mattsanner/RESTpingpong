<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<meta name="description" content="Xpanxion Ping Pong Records">
	<meta name="author" content="Sannman">

	<title>Home</title>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	

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
  <div class="site-wrapper">
    <div class="site-wrapper-inner">
      <div class="cover-container">
		<div class="masthead clearfix">
  		  <div class="inner">
	    	<h3 class="masthead-brand">Xpanxion Ping Pong Record Book</h3>	    	
	      		<nav>
				  <ul class="nav masthead-nav">
		  			<li id="HomeLink" onclick="HomeClick()" class="active"><a href="">Home</a></li>
		  			<li id="NavLink" onclick="NavClick()" class=""><a href="#">Navigation</a></li>
		  			<li id="RSLink" onclick="RSClick()" class=""><a href="#">Record Score</a></li>
				  </ul>
	      		</nav>
      	  </div>
	    </div>

	  <div class="inner cover" id="HomeText" style="display:inline">
	    <h1 class="cover-heading">Welcome</h1>
		<p>
			You have reached the record center for Xpanxion-Manhattan's Ping Pong matches.<br>
			For directions on reaching different records or recording matches go to the 
			"Navigation" tab.
		</p>	
	  </div>
	  
	  <div class="inner cover" id="NavText" style="display:none">
	    <h1 class="cover-heading">Site Navigation</h1>
			<h3>Leaderboard:</h3> 
			<!--  <p>Sorted by wins: /leaderboard_wins<br>Sorted by points scored: /leaderboards_points</p> -->
			<div class="btn-group">
				<button class="btn btn-primary" onclick="goToLeaderboardWins()" id="button_wins" type="button">By Wins</button>
				<button class="btn btn-primary" onclick="goToLeaderboardPoints()" id="button_points" type="button">By Points</button>
			</div>
			<!--<h3>Recording a Match:</h3>
			 <p>/record_match?player1=First Last&player2=First Last&score1=int&score2=int</p> -->
			<h3>Individual Match History:</h3>
			<!-- <p>/match_record?player1=First Last&player2=First Last</p>	-->
			
			<div class="btn-group" id="p1">
  				<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
  				<span class="selection" id="p1nameMatch">Player 1</span>
  				<span class="caret"></span>
  				</button>
  				<div class="dropdown-menu" aria-labelledby="menu1" id="p1dm"> <!-- role="menu" aria-labelledby="menu1" -->
  					<c:forEach items="${players }" var="player">  					
  						<li role="presentation" id="${player.firstName} ${player.lastName}"><a href="#">${player.firstName} ${player.lastName}</a></li>						
  					</c:forEach>
  				</div>
  			</div>
  			<div class="btn-group" id="p2">
  				<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
  				<span class="selection" id="p2nameMatch">Player 2</span>
  				<span class="caret"></span>
  				</button>
  				<div class="dropdown-menu" aria-labelledby="menu1" id="p2dm"> <!-- role="menu" aria-labelledby="menu1" -->
  					<c:forEach items="${players }" var="player">  					
  						<li role="presentation" id="${player.firstName} ${player.lastName}"><a href="#">${player.firstName} ${player.lastName}</a></li>
  						<div class="dropdown-divider"></div>  						
  					</c:forEach>
  				</div>
  			</div>
  			<div class="btn-group">
				<button class="btn btn-primary" onclick="lookupMatch()" id="recent_match" type="button">Lookup Match</button>
			</div>
			
			<h3>All Match History:</h3>			
			<div class="btn-group">
				<button class="btn btn-primary" onclick="goToAllMatches()" id="all_match" type="button">Show All Matches</button>
			</div>
			<h3>Recent Matches:</h3>
			<div class="btn-group">
				<button class="btn btn-primary" onclick="goToRecentMatches()" id="recent_match" type="button">Recent Matches</button>
			</div>
	  </div>
	  
	  <div class="inner cover" id="RSText" style="display:none">
  		<h1 class="cover-heading">Record a Score</h1>
  			<div class="btn-group" id="p1">
  				<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
  				<span class="selection" id="p1name">Player 1</span>
  				<span class="caret"></span>
  				</button>
  				<div class="dropdown-menu" aria-labelledby="menu1" id="p1dm"> <!-- role="menu" aria-labelledby="menu1" -->
  					<c:forEach items="${players }" var="player">  					
  						<li role="presentation" id="${player.firstName} ${player.lastName}"><a href="#">${player.firstName} ${player.lastName}</a></li>						
  					</c:forEach>
  				</div>  				
  			</div>
  			<input type="text" name="p1score" style="color:black;width:88px" placeholder="P1 Score">
  			<br>
  			<div class="btn-group" id="p2">
  				<button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
  				<span class="selection" id="p2name">Player 2</span>
  				<span class="caret"></span>
  				</button>
  				<div class="dropdown-menu" aria-labelledby="menu1" id="p2dm"> <!-- role="menu" aria-labelledby="menu1" -->
  					<c:forEach items="${players }" var="player">  					
  						<li role="presentation" id="${player.firstName} ${player.lastName}"><a href="#">${player.firstName} ${player.lastName}</a></li>
  						<div class="dropdown-divider"></div>  						
  					</c:forEach>
  				</div>
  			</div> 
  			<input type="text" name="p2score" style="color:black;width:88px" placeholder="P2 Score">
  			<br><br>
  			<button class="btn btn-primary" type="button" onclick="recordScore()">Record Score</button>  			
	  </div>
	  
	  <div class="mastfoot">
	  	<div class="inner">
	  		<p>Developed by Matt for Project Research</p>
	  		<p>Special Thanks to DJ for his web development help</p>
	  	</div>
	  </div>
	</div>
  </div>
</div>
	  
    <script type="text/javascript">
    	function HomeClick() {
    		var hl = document.getElementById("HomeLink");
    		if(hl.getAttribute("class") == "")   			
	   			var nt = document.getElementById("NavText");
	   			nt.style.display = "none";
	   			var rst = document.getElementById("RSText");
	   			rst.style.display = "none";
	   			var ht = document.getElementById("HomeText");
	   			ht.style.display = "inline";
	   			var nl = document.getElementById("NavLink");
	   			nl.setAttribute("class", "");
   				var rsl = document.getElementById("RSLink");
   				rsl.setAttribute("class", "");
   				hl.setAttribute("class", "active");
    	}
    	
    	function NavClick() {
    		var nl = document.getElementById("NavLink");
    		if(nl.getAttribute("class") == "")    			
	   			var ht = document.getElementById("HomeText");
	   			ht.style.display = "none";
	   			var rst = document.getElementById("RSText");
	   			rst.style.display = "none";
	   			var nt = document.getElementById("NavText");
	   			nt.style.display = "inline";	   			
   				var hl = document.getElementById("HomeLink");
   				hl.setAttribute("class", "");
   				var rsl = document.getElementById("RSLink");
   				rsl.setAttribute("class", "");   				
   				nl.setAttribute("class", "active");	
    	}
    	
    	function RSClick() {
    		var rsl = document.getElementById("RSLink");
    		if(rsl.getAttribute("class") == "")
    			var ht = document.getElementById("HomeText");
   				ht.style.display = "none";
   				var nt = document.getElementById("NavText");
	   			nt.style.display = "none";
	   			var rst = document.getElementById("RSText");
	   			rst.style.display = "inline";
    			var nl = document.getElementById("NavLink");
    			nl.setAttribute("class", "");
   				var hl = document.getElementById("HomeLink");
				hl.setAttribute("class", "");
				rsl.setAttribute("class", "active"); 
    	}
    	
    	function recordScore() {
    		 var URL = document.URL;
    		 var newURL = "";
    		 var i = 0;
    		 for(i = 0; i< URL.length - 1; i++)
   			 {
	 			 newURL = newURL + URL[i];
   			 }
    		 newURL = newURL + "record_match?player1=" + $(".selection#p1name").text() + "&player2=" + 
    				 $(".selection#p2name").text() + "&score1=" + $("[name='p1score']").val() + 
    				 "&score2=" + $("[name='p2score']").val();
    		 window.location = newURL;
    	}
    	
    	function goToLeaderboardWins() {
   		 var URL = document.URL;
   		 var newURL = "";
   		 var i = 0;
   		 for(i = 0; i< URL.length - 1; i++)
  			 {
	 			 newURL = newURL + URL[i];
  			 }
   		 newURL = newURL + "/leaderboard_wins";
   		 window.location = newURL;
   		}
    	
    	function goToLeaderboardPoints() {
      		 var URL = document.URL;
      		 var newURL = "";
      		 var i = 0;
      		 for(i = 0; i< URL.length - 1; i++)
     			 {
   	 			 newURL = newURL + URL[i];
     			 }
      		 newURL = newURL + "/leaderboard_points";
      		 window.location = newURL;
      	}
    	
    	function lookupMatch() {
   		 var URL = document.URL;
   		 var newURL = "";
   		 var i = 0;
   		 for(i = 0; i< URL.length - 1; i++)
  			 {
	 			 newURL = newURL + URL[i];
  			 }
   		 newURL = newURL + "match_record?player1=" + $(".selection#p1nameMatch").text() + "&player2=" + 
   				 $(".selection#p2nameMatch").text();
   		 window.location = newURL;
   		}
    	
    	function goToAllMatches() {
     		 var URL = document.URL;
     		 var newURL = "";
     		 var i = 0;
     		 for(i = 0; i< URL.length - 1; i++)
    			 {
  	 			 newURL = newURL + URL[i];
    			 }
     		 newURL = newURL + "/show_all_matches";
     		 window.location = newURL;
     	}
    	
    	function goToRecentMatches() {
    		 var URL = document.URL;
     		 var newURL = "";
     		 var i = 0;
     		 for(i = 0; i< URL.length - 1; i++)
    			 {
  	 			 newURL = newURL + URL[i];
    			 }
     		 newURL = newURL + "/recent_matches";
     		 window.location = newURL;
    	}
    	
    </script>
    <script>
    	$(".dropdown-menu#p1dm li a").click(function(){
   			$(this).parents("#p1").find('.selection').text($(this).text());
   			$(this).parents("#p1").find('.selection').val($(this).text());
    	});
    </script>
    
    <script>
    	$(".dropdown-menu#p2dm li a").click(function(){
   			$(this).parents("#p2").find('.selection').text($(this).text());
   			$(this).parents("#p2").find('.selection').val($(this).text());
    	});
    </script>
</body>
</html>
