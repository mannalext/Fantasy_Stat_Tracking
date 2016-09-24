package atm.stat.TrackerRobot;

public class TeamData {
	
	private int pointsFor, totalPointsFor, pointsAgainst, totalPointsAgainst, totalGames;
	private String name, division, opponent, location, result;
	/*make a 2 dimensional array here for matchup history
	first dimension is team name (so X indices where X is number of teams)
	second dimension is wins/losses against that particular team (2 indices)
*/

	
	public TeamData(String name, String division)
	{
		this.name = name;
		this.division = division;
	}
	
	
	public int getPointsFor()
	{
		return this.pointsFor;
	}
	private void setPointsFor(int value)
	{
		this.pointsFor= value;
	}
	
	public int getTotalPointsFor()
	{
		return this.totalPointsFor;
	}
	private void setTotalPointsFor()
	{
		this.totalPointsFor = this.totalPointsFor + this.pointsFor;
	}
	
	public int getPointsAgainst()
	{
		return this.pointsAgainst;
	}
	private void setPointsAgainst(int value)
	{
		this.pointsAgainst = value;
	}
	
	public int getTotalPointsAgainst()
	{
		return this.totalPointsAgainst;
	}
	private void setTotalPointsAgainst()
	{
		this.totalPointsAgainst = this.totalPointsAgainst + this.pointsAgainst;
	}
	
	public String getResult()
	{
		return this.result;
	}
	private void setResult()
	{
		if (getPointsFor() > getPointsAgainst())
		{
			this.result = "w";
		} else if (getPointsFor() < getPointsAgainst())
		{
			this.result = "l";
		} else if (getPointsFor() == getPointsAgainst())
		{
			this.result = "t";
		}
	}
	
	public String getOpponent()
	{
		return this.opponent;
	}
	private void setOpponent(String value)
	{
		opponent = value;
	}
	
	public int getTotalGames()
	{
		return this.totalGames;
	}
	private void setTotalGames(int value)
	{
		this.totalGames = value;
	}

	
}
