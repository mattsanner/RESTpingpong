package com.pingpong;

public class PingPongPlayer {
	
	private String firstName;
	private String lastName;
	private int wins;
	private int loss;
	private int scoreFor;
	private int scoreAgainst;
	
	public PingPongPlayer()
	{
		this.firstName = null;
		this.lastName = null;
		this.wins = -1;
		this.loss= -1;
		this.scoreFor = -1;
		this.scoreAgainst = -1;
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
	
	public void setLoss(int l)
	{
		this.loss = l;
	}
	
	public int getLoss()
	{
		return loss;
	}
	
	public void setScoreFor(int sf)
	{
		this.scoreFor = sf;
	}
	
	public int getScoreFor()
	{
		return scoreFor;
	}
	
	public void setScoreAgainst(int sa)
	{
		this.scoreAgainst = sa;
	}
	
	public int getScoreAgainst()
	{
		return scoreAgainst;
	}
}
