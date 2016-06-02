package project.pingpong.myapp;

import java.util.ArrayList;

import java.util.*;

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
	
	public void close()
	{
		session.close();
		cluster.close();
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	public void createSchema()
	{
		session.execute("CREATE KEYSPACE IF NOT EXISTS pingpong WITH replication " + "= {'class' : 'SimpleStrategy', 'replication_factor' : 3};");
		session.execute(
				"CREATE TABLE IF NOT EXISTS pingpong.players (" +					
					"firstName text, " +
					"lastName text, " +
					"wins int, " +
					"losses int," +
					"pointsFor bigint, " +
					"pointsAgainst bigint, " +
					"PRIMARY KEY (firstName, lastName));");
		/*session.execute(
				"CREATE TABLE IF NOT EXISTS pingpong.matches (" +
					"id int PRIMARY KEY," +
						"player1 text," +
						"player2 text" +
						"player1Wins int" +
						"player2Wins int);");		*/
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
	
	public void deleteRow(String firstName, String lastName)
	{		
		session.execute(
				"DELETE FROM pingpong.players WHERE " +
				"firstName = " + firstName + "AND lastName = " + lastName + ";");
	}	
}
