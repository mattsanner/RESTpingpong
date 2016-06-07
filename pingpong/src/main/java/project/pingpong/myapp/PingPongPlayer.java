package project.pingpong.myapp;
import java.text.DecimalFormat;
import java.util.Comparator;

public class PingPongPlayer {
	private String firstName;
	private String lastName;
	private int wins;
	private int losses;
	private double scoreFor;
	private double scoreAgainst;
	private DecimalFormat df;
	
	public PingPongPlayer()
	{
		this.firstName = null;
		this.lastName = null;
		this.wins = -1;
		this.losses= -1;
		df = new DecimalFormat();
		df.setMaximumFractionDigits(0);
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
	
	public String getFormattedScoreFor()
	{
		return df.format(scoreFor);
	}
	
	public String getFormattedScoreAgainst()
	{
		return df.format(scoreAgainst);
	}
	
	public static Comparator<PingPongPlayer> winComparator = new Comparator<PingPongPlayer>()
	{
		public int compare(PingPongPlayer p1, PingPongPlayer p2)
		{
			if(p1.wins == p2.wins)
			{
				if(p1.losses == p2.losses)
				{
					if(p1.scoreFor == p2.scoreFor)
					{
						return 0;
					}
					else
					{
						return p1.scoreFor < p2.scoreFor ? -1 : 1;
					}
				}
				else
				{
					return p1.losses > p2.losses ? -1 : 1;
				}
			}
			else
			{
				return p1.wins < p2.wins ? -1 : 1;
			}
		}
	};
	
	public static Comparator<PingPongPlayer> pointsComparator = new Comparator<PingPongPlayer>()
	{
		public int compare(PingPongPlayer p1, PingPongPlayer p2)
		{
			if(p1.scoreFor == p2.scoreFor)
			{
				if(p1.scoreAgainst == p2.scoreAgainst)
				{
					if(p1.wins == p2.wins)
					{
						return 0;
					}
					else
					{
						return p1.wins < p2.wins ? -1 : 1;
					}
				}
				else
				{
					return p1.scoreAgainst > p2.scoreAgainst ? -1 : 1;
				}
			}
			else
			{
				return p1.scoreFor < p2.scoreFor ? -1 : 1;
			}
		}
	};
}
