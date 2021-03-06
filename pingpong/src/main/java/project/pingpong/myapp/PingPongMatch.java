package project.pingpong.myapp;

import java.text.DecimalFormat;
import java.util.Date;

public class PingPongMatch {
	private String player1;
	private String player2;
	private double p1Score;
	private double p2Score;
	private int p1Wins;
	private int p2Wins;
	private Date date;
	private DecimalFormat df;
	
	public PingPongMatch()
	{
		df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
	}
	
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer2() {
		return player2;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	public double getP1Score() {
		return p1Score;
	}
	public void setP1Score(double p1Score) {
		this.p1Score = p1Score;
	}
	public double getP2Score() {
		return p2Score;
	}
	public void setP2Score(double p2Score) {
		this.p2Score = p2Score;
	}
	public int getP1Wins() {
		return p1Wins;
	}
	public void setP1Wins(int p1Wins) {
		this.p1Wins = p1Wins;
	}
	public int getP2Wins() {
		return p2Wins;
	}
	public void setP2Wins(int p2Wins) {
		this.p2Wins = p2Wins;
	}
	public String getFormattedP1Score()
	{
		return df.format(p1Score);
	}
	public String getFormattedP2Score()
	{
		return df.format(p2Score);
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date d)
	{
		this.date = d;
	}	
}
