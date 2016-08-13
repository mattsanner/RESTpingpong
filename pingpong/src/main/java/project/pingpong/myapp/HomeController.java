package project.pingpong.myapp;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application's pages (not just home)
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private PingPongClient client = new PingPongClient();
	private boolean initPassed = false;
	private boolean initCassandra = false;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {		
		if(!initPassed)
		{			
			try
			{
				InitializeClient();
				initPassed = client.initializeDatabase();
				CloseClient();
			}
			catch(Exception e)
			{
				model.addAttribute("error", "Cassandra is not initialized correctly, please restart Cassandra and try again.");				
				return "error";
			}
		}
//		if(!initCassandra)
//		{
//			//git ProcessBuilder pb = new ProcessBuilder("/home/matthew/Desktop/cassandra.sh");			
//		}
		
		InitializeClient();
		PingPongPlayer[] ppp = client.getPlayers();
		model.addAttribute("players", ppp);
		CloseClient();
		return "home";
	}
	
	@RequestMapping(value= "/record_match", params = {"player1", "player2", "score1", "score2"})
	public String record_match(@RequestParam("player1") String player1, @RequestParam("player2") String player2, @RequestParam("score1") String score1, @RequestParam("score2") String score2, Model model)
	{
		InitializeClient();
		
		//preliminary error checking on values
		if(player1.equals(null) || player2.equals(null) || player1.equals("") || player2.equals("")){
			model.addAttribute("error", "There was a problem with a player's name, check your values and retry");
			CloseClient();
			return "error";
		}
		if(player2.equals(player1)){
			model.addAttribute("error", "Cannot have a match between the same player.");
			CloseClient();
			return "error";
		}
		if(player1.equals("Player 1") || player2.equals("Player 2"))
		{
			model.addAttribute("error", "Please select a valid player from the drop down.\nIf no players can be selected please create players first.");
			CloseClient();
			return "error";
		}
			
		
		//ints for parsing score
		int sc1 = 0;
		int sc2 = 0;		
		
		try
		{
			sc1 = Integer.parseInt(score1);
			sc2 = Integer.parseInt(score2);
		}
		catch(Exception e)
		{	
			model.addAttribute("error", "Score could not be parsed");
			CloseClient();
			return "error";
		}
		
		if(sc1 == sc2)
		{
			model.addAttribute("error", "Score is invalid, no ties allowed.");
			CloseClient();
			return "error";
		} else if(sc1 > 11 || sc2 > 11)
		{
			if(Math.abs(sc1 - sc2) != 2){
				model.addAttribute("error", "Score is invalid");
				CloseClient();
				return "error";
			}
		}
		
		//Split params into first and last names for player lookup
		String[] firstLast = player1.split(" ");
		String player1First = firstLast[0];
		String player1Last = firstLast[1];
		firstLast = player2.split(" ");
		String player2First = firstLast[0];
		String player2Last = firstLast[1];
		
		PingPongPlayer ppp1 = client.getPlayer(player1First, player1Last);
		PingPongPlayer ppp2 = client.getPlayer(player2First, player2Last);
		
		if(!(ppp1 == null) && !(ppp2 ==null))
		{				
			if(sc1 < sc2)
			{				
				//Enter loss for player 1, sc1 is for, sc2 is against
				client.updatePlayerRecord(player1First, player1Last, sc1, sc2, false);
				//Enter win for player 2, sc2 is for, sc1 is against
				client.updatePlayerRecord(player2First, player2Last, sc2, sc1, true);
			}
			else if(sc1 > sc2)
			{
				//Enter win for player 1, sc1 is for, sc2 is against
				client.updatePlayerRecord(player1First, player1Last, sc1, sc2, true);
				//Enter loss for player 2, sc2 is for, sc1 is against
				client.updatePlayerRecord(player2First, player2Last, sc2, sc1, false);
			}
		}
		
		int result = client.recordMatch(player1First, player1Last, player2First, player2Last, sc1, sc2);
		//Error checking to provide user with different feedback
		if(result == -1)
		{
			model.addAttribute("error", "Player(s) in match do not exist, check spelling or create player before recording match.");
			CloseClient();
			return "error";
		}
		else if (result == -2)
		{
			model.addAttribute("error", "Error updating match records. Please check input values and try again.");
			CloseClient();
			return "error";
		}
		else
		{
			PingPongMatch ppm = client.getMatch((player1First + " " + player1Last), (player2First + " " + player2Last), true);
			if(ppm != null)
			{
				model.addAttribute("Match", ppm);
				CloseClient();
				return "record_match";
			}
			else
			{
				model.addAttribute("error", "The match did not load correctly. Please check input values.");
				CloseClient();
				return "error";
			}
		}
	}
	
	@RequestMapping(value="/match_record", params={"player1", "player2"})
	public String show_records(Model model, @RequestParam("player1") String p1, @RequestParam("player2") String p2)				//public PingPongPlayer[] show_records()
	{
		InitializeClient();
		PingPongMatch retVal = client.getMatch(p1, p2, false);
		CloseClient();
		model.addAttribute("match", retVal);		
		return "show_records";
	}
	
	@RequestMapping(value="/show_all_matches")
	public String show_all_matches(Model model)
	{
		InitializeClient();
		PingPongMatch[] ppm = client.getMatches();
		if(ppm.length > 0)
		{
			model.addAttribute("matches", ppm);
			CloseClient();
			return "show_all_matches";
		}
		else
		{
			model.addAttribute("error", "Matches did not load successfully.");
			CloseClient();
			return "error";
		}		
	}
	
	@RequestMapping(value="/create_player", params={"firstName", "lastName"})
	public String create_player(Model model, @RequestParam("firstName") String fN, @RequestParam("lastName") String lN)
	{
		InitializeClient();		
		if(client.getPlayer(fN, lN) != null)
		{
			 boolean flag = client.createPlayer(fN,lN);
			 if(flag)
			 {
				 PingPongPlayer p = client.getPlayer(fN, lN);
				 model.addAttribute("newPlayer", p);
			 }
			 else
			 {
				 model.addAttribute("error", "Error creating player.");
				 return "error";
			 }
			CloseClient();
			return "create_player";
		}
		else
		{
			model.addAttribute("error", "Player already exists!");
			CloseClient();
			return "error";
		}		 
	}
	
	@RequestMapping(value="/leaderboard_wins")
	public String leaderboard_wins(Model model)
	{
		InitializeClient();
		PingPongPlayer[] players = client.getPlayers();
		ArrayList<PingPongPlayer> sorted = new ArrayList<PingPongPlayer>();
		
		for(PingPongPlayer p : players)
		{
			sorted.add(p);
		}
		sorted.sort(PingPongPlayer.winComparator);
		players = sorted.toArray(players);
		PingPongPlayer[] flipped = new PingPongPlayer[players.length];
		for(int i = 0; i < players.length; i++)
		{
			flipped[players.length - 1 - i] = players[i];
		}
		model.addAttribute("players", flipped);
		model.addAttribute("leaderboard_type", "Wins");
		CloseClient();
		return "leaderboard";
	}
	
	@RequestMapping(value="/leaderboard_points")
	public String leaderboard_points(Model model)
	{
		InitializeClient();
		PingPongPlayer[] players = client.getPlayers();
		ArrayList<PingPongPlayer> sorted = new ArrayList<PingPongPlayer>();
		
		for(PingPongPlayer p : players)
		{
			sorted.add(p);
		}
		sorted.sort(PingPongPlayer.pointsComparator);
		players = sorted.toArray(players);
		PingPongPlayer[] flipped = new PingPongPlayer[players.length];
		for(int i = 0; i < players.length; i++)
		{
			flipped[players.length - 1 - i] = players[i];
		}
		model.addAttribute("players", flipped);
		model.addAttribute("leaderboard_type", "Points Scored");
		CloseClient();
		return "leaderboard";
	}	
	
	@RequestMapping(value="/recent_matches")
	public String recent_matches(Model model)
	{
		InitializeClient();
		PingPongMatch[] matches = client.getRecentMatches();
		//model.addAttribute("matches", flipped);
		model.addAttribute("matches", matches);
		CloseClient();
		return "recent_matches";
	}
	
	public void InitializeClient()
	{
		client.connect();		
	}
	
	public void CloseClient(){
		client.close();		
	}
}
