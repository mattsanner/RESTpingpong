package project.pingpong.myapp;

public class PingPongPlayer {
	private String firstName;
	private String lastName;
	private int wins;
	private int losses;
	private double scoreFor;
	private double scoreAgainst;
	
	public PingPongPlayer()
	{
		this.firstName = null;
		this.lastName = null;
		this.wins = -1;
		this.losses= -1;
	}
	
	public void setFirstName(String fn)
	{
		this.firstName = fn;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setLastName(String ln)
	{
		this.lastName = ln;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setWins(int w)
	{
		this.wins = w;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public void setLosses(int l)
	{
		this.losses = l;
	}
	
	public int getLosses()
	{
		return losses;
	}
	
	public void setScoreFor(double sf)
	{
		this.scoreFor = sf;
	}
	
	public double getScoreFor()
	{
		return scoreFor;
	}
	
	public void setScoreAgainst(double sa)
	{
		this.scoreAgainst = sa;
	}
	
	public double getScoreAgainst()
	{
		return scoreAgainst;
	}
}
