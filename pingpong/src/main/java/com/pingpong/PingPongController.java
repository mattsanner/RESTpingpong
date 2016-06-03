package com.pingpong;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.*;


@RestController
public class PingPongController {
	
	//private final AtomicLong counter = new AtomicLong();
	private PingPongClient client = new PingPongClient();
	//private boolean clientOpened = false;
	
	public void InitializeClient()
	{
		client.connect("127.0.0.1");
		
	}
	
	public void CloseClient(){
		client.close();
		
	}
	
	@RequestMapping(value= "/record_score", params = {"player1", "player2", "score1", "score2"})
	public PingPongPlayer[] recordScore(@RequestParam("player1") String player1, @RequestParam("player2") String player2, @RequestParam("score1") String score1, @RequestParam("score2") String score2)
	{
		InitializeClient();
		//String returnStr = "fail";
		int sc1 = 0;
		int sc2 = 0;
		boolean intCast = true;
		
		String[] firstLast = player1.split("_");
		String player1First = firstLast[0];
		String player1Last = firstLast[1];
		firstLast = player2.split("_");
		String player2First = firstLast[0];
		String player2Last = firstLast[1];
		
		ResultSet rs1 = client.querySchema(player1First, player1Last);
		ResultSet rs2 = client.querySchema(player2First, player2Last);
		
		try
		{
			sc1 = Integer.parseInt(score1);
			sc2 = Integer.parseInt(score2);
		}
		catch(Exception e)
		{	
			return new PingPongPlayer[0];
		}
		
		if(!player1.equals(null) && !player2.equals(null) && intCast)
		{				
			if(!resultSetEmpty(rs1) && !resultSetEmpty(rs2))
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
		}
		PingPongPlayer[] retVal = client.getPlayers();
		CloseClient();
		return retVal;
	}
	
	@RequestMapping("/show_records")
	public PingPongPlayer[] showPlayerRecords()
	{
		InitializeClient();
		PingPongPlayer[] retVal = client.getPlayers();
		CloseClient();
		return retVal;
	}
	
	public boolean resultSetEmpty(ResultSet r)
	{
		List<Row> rows = r.all();
		if(rows.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
}
