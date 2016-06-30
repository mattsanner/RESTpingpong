package project.pingpong.myapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class PingPongClient {
	private Cluster cluster;
	private Session session;
	
	public void connect(String node)
	{
		cluster = Cluster.builder().addContactPoint(node).build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Conected to cluster: %s\n", metadata.getClusterName());
		for(Host host : metadata.getAllHosts())
		{
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
		}		
		session = cluster.connect();
	}
	
	//DEBUG
	public boolean initializeDatabase()
	{
		try
		{
			session.execute("CREATE KEYSPACE IF NOT EXISTS pingpong WITH replication " + 
				      "= {'class':'SimpleStrategy', 'replication_factor':3};");
			session.execute("CREATE TABLE IF NOT EXISTS pingpong.players " +
							"(firstName text, lastName text, wins int, losses int, pointsFor double, pointsAgainst double, " + 
							"PRIMARY KEY (firstName, lastName));");
			session.execute("CREATE TABLE IF NOT EXISTS pingpong.matches " +
							"(player1 text, player2 text, p1score double, p2score double, p1wins int, p2wins int, " +
							"PRIMARY KEY (player1, player2));");
			session.execute("CREATE TABLE IF NOT EXISTS pingpong.matchList " +
							"(key int, time timestamp, player1 text, player2 text, p1score int, p2score int, " +
							"PRIMARY KEY (key, time));");
			session.execute("CREATE TABLE IF NOT EXISTS pingpong.matchKeys " +
							"(name text PRIMARY KEY, keycount counter);");
			session.execute("UPDATE pingpong.matchKeys SET keycount = keycount + 1 WHERE name = 'counter';");
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public void close()
	{
		session.close();
		cluster.close();
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	public void updatePlayerRecord(String firstName, String lastName, int pointsF, int pointsA, boolean win) 
	{
		ResultSet results = querySchema(firstName, lastName);
		int wins = 0;
		int losses = 0;
		Double pointsFor = 0.0;
		Double pointsAgainst = 0.0;
		for (Row r : results)
		{
			wins = r.getInt("wins");
			losses = r.getInt("losses");
			pointsFor = r.getDouble("pointsFor");
			pointsAgainst = r.getDouble("pointsAgainst");
		}
		
		pointsFor += pointsF;
		pointsAgainst += pointsA;
		if(win)
		{
			wins++;
			session.execute("UPDATE pingpong.players SET wins = " + wins + " WHERE lastName = '" + lastName + "' AND firstName = '" + firstName +  "';");
		}
		else
		{
			losses++;
			session.execute("UPDATE pingpong.players SET losses = " + losses + " WHERE lastName = '" + lastName + "' AND firstName = '" + firstName +  "';");
		}
		session.execute("UPDATE pingpong.players SET pointsFor = " + pointsFor + " WHERE lastName = '" + lastName + "' AND firstName = '" + firstName +  "';");
		session.execute("UPDATE pingpong.players SET pointsAgainst = " + pointsAgainst + " WHERE lastName = '" + lastName + "' AND firstName = '" + firstName +  "';");
	}
	
	public ResultSet querySchema(String firstName, String lastName)
	{
		ResultSet results = session.execute(
									"SELECT * FROM pingpong.players " +
									"WHERE firstName = '" + firstName + 
									"' AND lastName = '" +lastName+ "';");
		return results;
	}
	
	public ResultSet querySchema()
	{
		ResultSet results = session.execute("SELECT * FROM pingpong.players;");
		return results;
	}
	
	public PingPongPlayer[] getPlayers()
	{
		ArrayList<PingPongPlayer> p = new ArrayList<PingPongPlayer>();		
		ResultSet results = querySchema();
		
		for(Row r : results)
		{
			PingPongPlayer play = new PingPongPlayer();
			play.setFirstName(r.getString("firstName"));
			play.setLastName(r.getString("lastName"));
			play.setWins(r.getInt("wins"));
			play.setLosses(r.getInt("losses"));
			play.setScoreFor(r.getDouble("pointsFor"));							
			play.setScoreAgainst(r.getDouble("pointsAgainst"));
			
			p.add(play);
		}		
		
		PingPongPlayer[] ppp = new PingPongPlayer[p.size()];
		return p.toArray(ppp);
	}
	
	public PingPongPlayer getPlayer(String firstName, String lastName)
	{
		PingPongPlayer play = new PingPongPlayer();
		ResultSet result = querySchema(firstName, lastName);
		for(Row r : result)
		{
			play.setFirstName(r.getString("firstName"));
			play.setLastName(r.getString("lastName"));
			play.setWins(r.getInt("wins"));
			play.setLosses(r.getInt("losses"));
			play.setScoreFor(r.getDouble("pointsFor"));							
			play.setScoreAgainst(r.getDouble("pointsAgainst"));			
			break;
		}
		return play;
	}
	
	public PingPongMatch getMatch(String player1, String player2, boolean creatingMatch)
	{
		PingPongMatch match = new PingPongMatch();
		ResultSet rs = session.execute("SELECT * FROM pingpong.matches WHERE " +
							"player1 = '" + player1 + "' AND player2 = '" + player2 +
							"';");
		if(rs.isExhausted())
		{
			//Swap players to check if the record has players switched
			rs = session.execute("SELECT * FROM pingpong.matches WHERE " +
					"player1 = '" + player2 + "' AND player2 = '" + player1 +
					"';");
			if(rs.isExhausted())
			{
				String[] splitString = player1.split(" ");			
				PingPongPlayer p = getPlayer(splitString[0], splitString[1]);
				String[] splitString2 = player2.split(" ");
				PingPongPlayer p2 = getPlayer(splitString2[0], splitString2[1]);
				if(p.getWins() == -1 || p2.getWins() == -1)
				{
					return null;
				}
				else
				{
					if(creatingMatch)
					{
						createMatch(player1, player2);
						rs = session.execute("SELECT * FROM pingpong.matches WHERE " +
								"player1 = '" + player1 + "' AND player2 = '" + player2 +
								"';");
					}
				}
			}
		}
		
		for(Row r : rs)
		{
			match.setPlayer1(r.getString("player1"));
			match.setPlayer2(r.getString("player2"));
			match.setP1Score(r.getDouble("p1Score"));
			match.setP1Wins(r.getInt("p1wins"));
			match.setP2Score(r.getDouble("p2Score"));
			match.setP2Wins(r.getInt("p2wins"));
		}		
		return match;
	}
	
	public PingPongMatch[] getMatches()
	{
		ResultSet rs = session.execute("SELECT * FROM pingpong.matches;");
		List<Row> rowList = rs.all();
		PingPongMatch[] ppm = new PingPongMatch[rowList.size()];
		int i = 0;
		
		for(Row r : rowList)
		{
			ppm[i] = new PingPongMatch();
			ppm[i].setPlayer1(r.getString("player1"));
			ppm[i].setPlayer2(r.getString("player2"));
			ppm[i].setP1Score(r.getDouble("p1Score"));
			ppm[i].setP1Wins(r.getInt("p1wins"));
			ppm[i].setP2Score(r.getDouble("p2Score"));
			ppm[i].setP2Wins(r.getInt("p2wins"));
			i++;
		}
		
		return ppm;
	}
	
	public boolean createPlayer(String fN, String lN)
	{
		try
		{
			session.execute("INSERT INTO pingpong.players (firstName, lastName, wins, losses, pointsFor, pointsAgainst) VALUES ('" + fN + "', '" + lN + "', 0, 0, 0, 0);");
		}
		catch(Exception e)
		{
			return false;
		}
		return true;				
	}
	
	public int recordMatch(String p1First, String p1Last, String p2First, String p2Last, int p1Score, int p2Score)
	{
		try
		{
		String p1 = p1First + " " + p1Last;
		String p2 = p2First + " " + p2Last;
		
		PingPongMatch ppm = getMatch(p1, p2, true);
		boolean flipped = false;
				
		
		//Make sure match DB query was successful
		if(ppm != null)
		{
			flipped = ppm.getPlayer1().equals(p2);
			if(p1Score > p2Score && !flipped)
			{
				ppm.setP1Wins(ppm.getP1Wins() + 1);
				ppm.setP1Score(ppm.getP1Score() + p1Score);
				ppm.setP2Score(ppm.getP2Score() + p2Score);
			}
			else if(p1Score < p2Score && !flipped)
			{
				ppm.setP2Wins(ppm.getP2Wins() + 1);
				ppm.setP1Score(ppm.getP1Score() + p1Score);
				ppm.setP2Score(ppm.getP2Score() + p2Score);
			}
			//If DB record has players flipped
			else if(p1Score > p2Score && flipped)
			{
				ppm.setP2Wins(ppm.getP2Wins() + 1);
				ppm.setP1Score(ppm.getP1Score() + p2Score);
				ppm.setP2Score(ppm.getP2Score() + p1Score);
			}
			else if(p1Score < p2Score && flipped)
			{
				ppm.setP1Wins(ppm.getP1Wins() + 1);
				ppm.setP1Score(ppm.getP1Score() + p2Score);
				ppm.setP2Score(ppm.getP2Score() + p1Score);
			}		
		}
		else
		{
			return -1;
		}
		
		//Execute UPDATE
		//Update in player vs player - pingpong.matches DB
		if(!flipped)
		{
		String exString = ("UPDATE pingpong.matches " +
						"SET p1score = " + ppm.getP1Score() + ", p2Score = " + ppm.getP2Score() +
						", p1wins = " + ppm.getP1Wins() + ", p2wins = " + ppm.getP2Wins() +
						" WHERE player1 = '" + p1 + "' AND player2 = '" + p2 + "';");
		session.execute(exString);
		//int i = getMatchListKey();
		exString = ("INSERT INTO pingpong.matchList " + 
							"(key, time, p1score, p2score, player1, player2) " +
							"VALUES (1, toTimestamp(now()), " + p1Score + ", " + p2Score + ", '" +
							p1 + "', '" + p2 +"');");	
		session.execute(exString);
		}
		else
		{
			String exString = ("UPDATE pingpong.matches " +
					"SET p1score = " + ppm.getP1Score() + ", p2Score = " + ppm.getP2Score() +
					", p1wins = " + ppm.getP1Wins() + ", p2wins = " + ppm.getP2Wins() +
					" WHERE player1 = '" + p2 + "' AND player2 = '" + p1 + "';");
			session.execute(exString);
			//int i = getMatchListKey();
			exString = ("INSERT INTO pingpong.matchList " + 
						"(key, time, p1score, p2score, player1, player2) " +
						"VALUES (1, toTimestamp(now()), " + p2Score + ", " + p1Score + ", '" +
						p2 + "', '" + p1 +"');");	
			session.execute(exString);
		}
		}	
		catch(Exception e)
		{
			return -2;
		}
		return 0;
	}
	
	public PingPongMatch[] getRecentMatches()
	{
		ResultSet rs = session.execute("SELECT * FROM pingpong.matchList WHERE key=1 ORDER BY time DESC LIMIT 15;");
		List<Row> rowList = rs.all();
		PingPongMatch[] matches;
		if(rowList.size() > 10)
		{
			matches = new PingPongMatch[10];
		}
		else
		{
			matches = new PingPongMatch[rowList.size()];
		}
		int i = 0;
		for(Row r : rowList)
		{
			if(i>10)
			{
				break;
			}
			matches[i] = new PingPongMatch();
			matches[i].setPlayer1(r.getString("player1"));
			matches[i].setPlayer2(r.getString("player2"));
			matches[i].setP1Score(r.getInt("p1score"));
			matches[i].setP2Score(r.getInt("p2score"));
			Object o = r.getObject("time");
			matches[i].setDate((Date) o);
			i++;
		}
		
		return matches;
	}
	
	public boolean createMatch(String p1, String p2)
	{
		try
		{
			session.execute("INSERT INTO pingpong.matches (player1, player2, p1score, p1wins, p2score, p2wins)" +
							"VALUES ('" + p1 + "', '" + p2 + "', 0, 0, 0, 0);");
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public void deletePlayer(String firstName, String lastName)
	{		
		session.execute(
				"DELETE FROM pingpong.players WHERE " +
				"firstName = " + firstName + "AND lastName = " + lastName + ";");
	}
	public int getMatchListKey()
	{
		ResultSet rs = session.execute("SELECT * FROM pingpong.matchKeys;");
		List<Row> rl = rs.all();
		session.execute("UPDATE pingpong.matchKeys SET keycount = keycount + 1 WHERE name = 'counter';");
		int temp = Integer.parseInt(rl.get(0).getObject("keycount").toString());
		return temp;
	}
}
