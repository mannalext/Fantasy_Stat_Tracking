package atm.stat.TrackerRobot;

public class TeamData {
	
	private static int pointsFor, totalPointsFor, avgPointsFor, highPointsFor, lowPointsFor, pointsAgainst,
	totalPointsAgainst, avgPointsAgainst, highPointsAgainst, lowPointsAgainst, margin, totalMarginVictory, highMarginVictory, avgMarginVictory,
	lowMarginVictory, totalMarginDefeat, highMarginDefeat, avgMarginDefeat, lowMarginDefeat, pointDifferential, totalGames, wins, losses,
	homeWins, awayWins, divWins, nonDivWins, homeGames, awayGames, divGames, nonDivGames;
	
	private static String name, division, opponent, location, result;
	/*make a 2 dimensional array here for matchup history
	first dimension is team name (so X indices where X is number of teams)
	second dimension is wins/losses against that particular team (2 indices)
*/

	
	//constructor
	public TeamData(String nameArg, String divisionArg)
	{
		name = nameArg;
		division = divisionArg;
	}
	
	
	public String getOpponent()
	{
		return opponent;
	}
	private void setOpponent(String value)
	{
		opponent = value;
	}
	
	public String getLocation()
	{
		return location;
	}
	private void setLocation(String value)
	{
		location = value;
	}
	
	public int getPointsFor()
	{
		return pointsFor;
	}
	private void setPointsFor(int value)
	{
		pointsFor= value;
	}
	
	public int getTotalPointsFor()
	{
		return totalPointsFor;
	}
	private void setTotalPointsFor()
	{
		totalPointsFor = totalPointsFor + pointsFor;
	}
	
	public int getAvgPointsFor()
	{
		return avgPointsFor;
	}
	private void setAvgPointsFor()
	{
		avgPointsFor = totalPointsFor / totalGames;
	}
	
	public int getHighPointsFor()
	{
		return highPointsFor;
	}
	private void setHighPointsFor()
	{
		if (pointsFor > highPointsFor)
		{
			highPointsFor = pointsFor;
		}
	}
	
	public int getLowPointsFor()
	{
		return lowPointsFor;
	}
	private void setLowPointsFor()
	{
		if (pointsFor < lowPointsFor)
		{
			lowPointsFor = pointsFor;
		}
	}
	
	public int getPointsAgainst()
	{
		return pointsAgainst;
	}
	private void setPointsAgainst(int value)
	{
		pointsAgainst = value;
	}
	
	public int getTotalPointsAgainst()
	{
		return totalPointsAgainst;
	}
	private void setTotalPointsAgainst()
	{
		totalPointsAgainst = totalPointsAgainst + pointsAgainst;
	}
	
	public int getAvgPointsAgainst()
	{
		return avgPointsAgainst;
	}
	private void setAvgPointsAgainst()
	{
		avgPointsAgainst = totalPointsAgainst / totalGames;
	}
	
	public int getHighPointsAgainst()
	{
		return highPointsAgainst;
	}
	private void setHighPointsAgainst()
	{
		if (pointsAgainst > highPointsAgainst)
		{
			highPointsAgainst = pointsAgainst;
		}
	}
	
	public int getLowPointsAgainst()
	{
		return lowPointsAgainst;
	}
	private void setLowPointsAgainst()
	{
		if (pointsAgainst < lowPointsAgainst)
		{
			lowPointsAgainst = pointsAgainst;
		}
	}
	
	//when deciding whether to get margin victory or defeat, do a check on the return value of getMargin to see if it's positive or negative
	public int getMargin()
	{
		return margin;
	}
	private void setMargin()
	{
		margin = pointsFor - pointsAgainst;
	}
	
	public int getTotalMarginVictory()
	
	private void setTotalMarginVictory()
	
	public int getHighMarginVictory()
	{
		return highMarginVictory;
	}
	private void setHighMarginVictory()
	{
		if (margin > highMarginVictory)
		{
			highMarginVictory = margin;
		}
	}
	
	public int getLowMarginVictory()
	
	private void setLowMarginVictory()
	
	
	public int getAvgMarginVictory()
	
	private void setAvgMarginVictory()
	
	
	public int getTotalMarginDefeat()
	
	private void setTotalMarginDefeat()
	
	
	public int getHighMarginDefeat()
	
	private void setHighMarginDefeat()
	
	
	public int getLowMarginDefeat()
	
	private void setLowMarginDefeat()
	
	
	public int getAvgMarginDefeat()
	
	private void setAvgMarginDefat()
	
	
	public int getPointDifferential()
	
	private void setPointDifferential()
	
	
	public String getResult()
	{
		return result;
	}
	private void setResult()
	{
		if (pointsFor > pointsAgainst)
		{
			result = "w";
		} else if (pointsFor < pointsAgainst)
		{
			result = "l";
		} else if (pointsFor == pointsAgainst)
		{
			result = "t";
		}
	}
	
	public int getWins()
	{
		return wins;
	}
	private void setWins()
	{
		wins++;
	}
	
	public int getLosses()
	{
		return losses;
	}
	private void setLosses()
	{
		losses++;
	}
	
	public int getTotalGames()
	{
		return totalGames;
	}
	private void setTotalGames()
	{
		totalGames++;
	}
	
	public int getHomeGames()
	{
		return homeGames;
	}
	private void setHomeGames()
	{
		homeGames++;
	}
	
	public int getHomeWins()
	{
		return homeWins;
	}
	private void setHomeWins()
	{
		homeWins++;
	}
	
	public int getAwayGames()
	{
		return awayGames;
	}
	private void setAwayGames()
	{
		awayGames++;
	}
	
	public int getAwayWins()
	{
		return awayWins;
	}
	private void setAwayWins()
	{
		awayWins++;
	}
	
	public int getDivGames()
	{
		return divGames;
	}
	private void setDivGames()
	{
		divGames++;
	}
	
	public int getDivWins()
	{
		return divWins;
	}
	private void setDivWins()
	{
		divWins++;
	}
	
	public int getNonDivGames()
	{
		return nonDivGames;
	}
	private void setNonDivGames()
	{
		nonDivGames++;
	}
	
	public int getNonDivWins()
	{
		return nonDivWins;
	}
	private void setNonDivWins()
	{
		nonDivWins++;
	}
	
	
	
	
	
	
	
	
	//calculation functions
	public double calculateOverallWinPct()
	{
		return (getWins() / getTotalGames());
	}
	
	public double calculateHomeWinPct()
	{
		return (getHomeWins() / getHomeGames());
	}
	
	public double calculateAwayWinPct()
	{
		return (getAwayWins() / getAwayGames());
	}
	
	public double calculateDivWinPct()
	{
		return (getDivWins() / getDivGames());
	}
	
	public double calculateNonDivWinPct()
	{
		return (getNonDivWins() / getNonDivGames());
	}

	

	
}
