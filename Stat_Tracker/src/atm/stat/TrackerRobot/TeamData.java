package atm.stat.TrackerRobot;

public class TeamData {
	
	private static int pointsFor, totalPointsFor, avgPointsFor, highPointsFor, 
	lowPointsFor, pointsAgainst, totalPointsAgainst, avgPointsAgainst,
	highPointsAgainst, lowPointsAgainst, totalHomePoints, avgHomePoints, totalAwayPoints, avgAwayPoints,
	margin, totalMarginVictory, highMarginVictory, avgMarginVictory,
	lowMarginVictory, totalMarginDefeat, highMarginDefeat, avgMarginDefeat, 
	lowMarginDefeat, pointDifferential, totalGames, wins, losses,
	homeWins, awayWins, divWins, nonDivWins, homeGames, awayGames, divGames, 
	nonDivGames;
	
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
	
	public int getTotalHomePoints()
	{
		return totalHomePoints;
	}
	private void setTotalHomePoints()
	{
		if (location == "home")
		{
			totalHomePoints += pointsFor;
		}
	}
	
	public int getAvgHomePoints() 
	{
		return avgHomePoints;
	}
	private void setAvgHomePoints()
	{
		avgHomePoints = totalHomePoints / homeGames;
	}
	
	public int getTotalAwayPoints()
	{
		return totalAwayPoints;
	}
	private void setTotalAwayPoints()
	{
		if (location == "away")
		{
			totalAwayPoints += pointsFor;
		}
	}
	
	public int getAvgAwayPoints()
	{
		return avgAwayPoints;
	}
	private void setAvgAwayPoints()
	{
		avgAwayPoints = totalAwayPoints / awayGames;
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
	{
		return totalMarginVictory;
	}
	
	private void setTotalMarginVictory()
	{
		if (margin > 0) 
		{
			totalMarginVictory += margin;
		}
	}
	
	public int getHighMarginVictory()
	{
		return highMarginVictory;
	}
	private void setHighMarginVictory()
	{
		if ((margin > highMarginVictory) && (margin > 0))
		{
			highMarginVictory = margin;
		}
	}
	
	public int getLowMarginVictory()
	{
		return lowMarginVictory;
	}
	
	private void setLowMarginVictory()
	{
		if ((margin < lowMarginVictory) && (margin > 0))
		{
			lowMarginVictory = margin;
		}
	}
	
	public int getAvgMarginVictory()
	{
		return avgMarginVictory;
	}
	
	private void setAvgMarginVictory()
	{
		avgMarginVictory = totalMarginVictory / wins;
	}
	
	public int getTotalMarginDefeat()
	{
		return totalMarginDefeat;
	}
	
	private void setTotalMarginDefeat()
	{
		if (margin < 0)
		{
			totalMarginDefeat += margin;
		}
	}
	
	public int getHighMarginDefeat()
	{
		return highMarginDefeat;
	}
	
	private void setHighMarginDefeat()
	{
		if ((margin < highMarginDefeat) && margin < 0)
		{
			highMarginDefeat = margin;
		}
	}
	
	public int getLowMarginDefeat()
	{
		return lowMarginDefeat;
	}
	
	private void setLowMarginDefeat()
	{
		if (margin < 0 && (margin > lowMarginDefeat))
		{
			lowMarginDefeat = margin;
		}
	}
	
	public int getAvgMarginDefeat()
	{
		return avgMarginDefeat;
	}
	
	private void setAvgMarginDefeat()
	{
		avgMarginDefeat = totalMarginDefeat / losses;
	}
		
	public int getPointDifferential()
	{
		return pointDifferential;
	}
	
	private void setPointDifferential()
	{
		pointDifferential += margin;
	}
	
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
