package project.pingpong.myapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PingPongClient {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public void connect()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/pingpong?user=root&password=xpanxion");
			System.out.println("Connection to pingpong successful.");
			statement = connect.createStatement();
			System.out.println("Statement object created.");
		} catch (Exception e) {
			System.out.println("Error during connection.");
		}
	}
	
	//DEBUG
	public boolean initializeDatabase()
	{
		try
		{
			//session.execute("CREATE DATABASE IF NOT EXISTS pingpong WITH replication " + 
			//	      "= {'class':'SimpleStrategy', 'replication_factor':3};");
			statement.executeQuery("CREATE TABLE IF NOT EXISTS pingpong.players " +
							"(firstName text, lastName text, wins int, losses int, pointsFor double, pointsAgainst double, " + 
							"PRIMARY KEY (firstName, lastName));");
			statement.executeQuery("CREATE TABLE IF NOT EXISTS pingpong.matches " +
							"(player1 text, player2 text, p1score double, p2score double, p1wins int, p2wins int, " +
							"PRIMARY KEY (player1, player2));");
			statement.executeQuery("CREATE TABLE IF NOT EXISTS pingpong.matchList " +
							"(key int, time timestamp, player1 text, player2 text, p1score int, p2score int, " +
							"PRIMARY KEY (key, time));");
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public void close()
	{
	    try {
	        if (resultSet != null) {
	          resultSet.close();
	        }

	        if (statement != null) {
	          statement.close();
	        }

	        if (connect != null) {
	          connect.close();
	        }
	      } catch (Exception e) {
	    	  System.out.println("Error closing system");
	      }
	}
	
	public void updatePlayerRecord(String firstName, String lastName, int pointsF, int pointsA, boolean win) 
	{
		ResultSet results = querySchema(firstName, lastName);
		int wins = 0;
		int losses = 0;
		Double pointsFor = 0.0;
		Double pointsAgainst = 0.0;
		/*for (Row r : results)
		{
			wins = r.getInt("wins");
			losses = r.getInt("losses");
			pointsFor = r.getDouble("pointsFor");
			pointsAgainst = r.getDouble("pointsAgainst");
		}*/
		try{
		while(results.next()){
			wins = results.getInt("wins");
			losses = results.getInt("losses");
			pointsFor = results.getDouble("pointsfor");
			pointsAgainst = results.getDouble("pointsagainst");
		}
		
		pointsFor += pointsF;
		pointsAgainst += pointsA;
		if(win)
		{
			wins++;
			System.out.println("UPDATE pingpong.players SET wins = " + wins + " WHERE lastname = '" + lastName + "' AND firstname = '" + firstName +  "';");
			statement.executeUpdate("UPDATE pingpong.players SET wins = " + wins + " WHERE lastname = '" + lastName + "' AND firstname = '" + firstName +  "';");
		}
		else
		{
			losses++;
			statement.executeUpdate("UPDATE pingpong.players SET losses = " + losses + " WHERE lastname = '" + lastName + "' AND firstname = '" + firstName +  "';");
		}
		statement.executeUpdate("UPDATE pingpong.players SET pointsfor = " + pointsFor + " WHERE lastname = '" + lastName + "' AND firstname = '" + firstName +  "';");
		statement.executeUpdate("UPDATE pingpong.players SET pointsagainst = " + pointsAgainst + " WHERE lastname = '" + lastName + "' AND firstname = '" + firstName +  "';");
		}catch(Exception e) 
		{
			System.out.println("Error querying database");
		}
	}
	
	public ResultSet querySchema(String firstName, String lastName)
	{
		try{
		ResultSet results = statement.executeQuery(
									"SELECT * FROM pingpong.players " +
									"WHERE firstname = '" + firstName + 
									"' AND lastname = '" +lastName+ "';");
		return results;
		} catch(Exception e){
			System.out.println("Error at querySchema. (overloaded w/ player name");
			return null;
		}
	}
	
	public ResultSet querySchema()
	{
		try{
			ResultSet results = statement.executeQuery("SELECT * FROM pingpong.players;");
			return results;
		}catch (Exception e) {
			System.out.println("Error in querySchema (all)");
			return null;
		}
	}
	
	public PingPongPlayer[] getPlayers()
	{
		ArrayList<PingPongPlayer> p = new ArrayList<PingPongPlayer>();		
		ResultSet results = querySchema();
		if(results != null) {
			try {
			while(results.next())
			{
				PingPongPlayer play = new PingPongPlayer();
				play.setFirstName(results.getString("firstname"));
				play.setLastName(results.getString("lastname"));
				play.setWins(results.getInt("wins"));
				play.setLosses(results.getInt("losses"));
				play.setScoreFor(results.getDouble("pointsfor"));							
				play.setScoreAgainst(results.getDouble("pointsagainst"));
				
				p.add(play);
			}		
			
			PingPongPlayer[] ppp = new PingPongPlayer[p.size()];
			return p.toArray(ppp);
			} catch (Exception e) {
				return null;
			}
		} else
		{
			return null;
		}
	}
	
	public PingPongPlayer getPlayer(String firstName, String lastName)
	{
		PingPongPlayer play = new PingPongPlayer();
		ResultSet result = querySchema(firstName, lastName);
		try {
		while(result.next())
		{
			play.setFirstName(result.getString("firstName"));
			play.setLastName(result.getString("lastName"));
			play.setWins(result.getInt("wins"));
			play.setLosses(result.getInt("losses"));
			play.setScoreFor(result.getDouble("pointsFor"));							
			play.setScoreAgainst(result.getDouble("pointsAgainst"));			
			break;
		}
			return play;
		} catch (Exception e) {
			
		}
			return null;
	}
	
	public PingPongMatch getMatch(String player1, String player2, boolean creatingMatch)
	{
		PingPongMatch match = new PingPongMatch();
		try{
			ResultSet rs = statement.executeQuery("SELECT * FROM pingpong.matches WHERE " +
								"player1 = '" + player1 + "' AND player2 = '" + player2 +
								"';");
			boolean hasResults = rs.next();
			boolean hasResults2 = true;
			if(!hasResults)
			{
				//Swap players to check if the record has players switched
				rs = statement.executeQuery("SELECT * FROM pingpong.matches WHERE " +
						"player1 = '" + player2 + "' AND player2 = '" + player1 +
						"';");
				hasResults2 = rs.next();
				hasResults = hasResults2;
				if(!hasResults2)
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
							rs = statement.executeQuery("SELECT * FROM pingpong.matches WHERE " +
									"player1 = '" + player1 + "' AND player2 = '" + player2 +
									"';");
							hasResults2 = rs.next();
							hasResults = hasResults2;
						}
					}
				}
			}
			
			while(hasResults && hasResults2)
			{
				match.setPlayer1(rs.getString("player1"));
				match.setPlayer2(rs.getString("player2"));
				match.setP1Score(rs.getDouble("p1Score"));
				match.setP1Wins(rs.getInt("p1wins"));
				match.setP2Score(rs.getDouble("p2Score"));
				match.setP2Wins(rs.getInt("p2wins"));
				hasResults=rs.next();
			}		
			return match;
		} catch( Exception e) {
			return null;
		}
	}
	
	public PingPongMatch[] getMatches()
	{
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM pingpong.matches;");
			List<PingPongMatch> ppmAL = new ArrayList<PingPongMatch>();
			int i = 0;
			while(rs.next())
			{
				PingPongMatch ppmi = new PingPongMatch();
				ppmi.setPlayer1(rs.getString("player1"));
				ppmi.setPlayer2(rs.getString("player2"));
				ppmi.setP1Score(rs.getDouble("p1Score"));
				ppmi.setP1Wins(rs.getInt("p1wins"));
				ppmi.setP2Score(rs.getDouble("p2Score"));
				ppmi.setP2Wins(rs.getInt("p2wins"));
				ppmAL.add(ppmi);
			}
			PingPongMatch[] ppm = new PingPongMatch[ppmAL.size()];
			ppm = ppmAL.toArray(ppm);
			return ppm;
		} catch(Exception e) {
			return null;
		}
	}
	
	public boolean createPlayer(String fN, String lN)
	{
		try
		{
			statement.executeUpdate("INSERT INTO pingpong.players (firstName, lastName, wins, losses, pointsFor, pointsAgainst) VALUES ('" + fN + "', '" + lN + "', 0, 0, 0, 0);");
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
		statement.executeUpdate(exString);
		//int i = getMatchListKey();
		exString = ("INSERT INTO pingpong.matchList " + 
							"(matchKey, time, p1score, p2score, player1, player2) " +
							"VALUES (1, NOW(), " + p1Score + ", " + p2Score + ", '" +
							p1 + "', '" + p2 +"');");	
		statement.executeUpdate(exString);
		}
		else
		{
			String exString = ("UPDATE pingpong.matches " +
					"SET p1score = " + ppm.getP1Score() + ", p2Score = " + ppm.getP2Score() +
					", p1wins = " + ppm.getP1Wins() + ", p2wins = " + ppm.getP2Wins() +
					" WHERE player1 = '" + p2 + "' AND player2 = '" + p1 + "';");
			statement.executeUpdate(exString);
			//int i = getMatchListKey();
			exString = ("INSERT INTO pingpong.matchList " + 
						"(matchKey, time, p1score, p2score, player1, player2) " +
						"VALUES (1, NOW(), " + p2Score + ", " + p1Score + ", '" +
						p2 + "', '" + p1 +"');");	
			statement.executeUpdate(exString);
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
		try {
		ResultSet rs = statement.executeQuery("SELECT * FROM pingpong.matchList WHERE matchKey=1 ORDER BY time DESC LIMIT 15;");
		//List<Row> rowList = rs.all();
		rs.last();
		int count = rs.getRow();
		rs = statement.executeQuery("SELECT * FROM pingpong.matchList WHERE matchKey=1 ORDER BY time DESC LIMIT 15;");
		PingPongMatch[] matches;
		if(count > 15)
		{
			matches = new PingPongMatch[15];
		}
		else
		{
			matches = new PingPongMatch[count];
		}
		int i = 0;
		while(rs.next())
		{
			if(i>15)
			{
				break;
			}
			matches[i] = new PingPongMatch();
			matches[i].setPlayer1(rs.getString("player1"));
			matches[i].setPlayer2(rs.getString("player2"));
			matches[i].setP1Score(rs.getInt("p1score"));
			matches[i].setP2Score(rs.getInt("p2score"));
			Object o = rs.getObject("time");
			matches[i].setDate((Date) o);
			i++;
		}
		
		return matches;
		} catch(Exception e) {
			System.out.println("Error in getRecentMatches");
			return null;
		}
	}
	
	public boolean createMatch(String p1, String p2)
	{
		try
		{
			statement.executeUpdate("INSERT INTO pingpong.matches (player1, player2, p1score, p1wins, p2score, p2wins)" +
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
		try{
		statement.executeQuery(
				"DELETE FROM pingpong.players WHERE " +
				"firstName = " + firstName + "AND lastName = " + lastName + ";");
		} catch (Exception e) {
			System.out.println("Error deleting player");
		}
	}
}
