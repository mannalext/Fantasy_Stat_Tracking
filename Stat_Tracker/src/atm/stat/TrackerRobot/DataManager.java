package atm.stat.TrackerRobot;

public class DataManager {
	
/*	//THIS IS AN EXAMPLE GETTER AND SETTER
	private String myField; //"private" means access to this is restricted

	public String getMyField()
	{
	     //include validation, logic, logging or whatever you like here
	    return this.myField;
	}
	public void setMyField(String value)
	{
	     //include more logic
	     this.myField = value;
	}*/
	
	
	private int teamCount;
	public int getTeamCount()
	{
		return this.teamCount;
	}
	public void setTeamCount(int value)
	{
		this.teamCount = value;
	}
	
	private int numWeeks;
	public int getNumWeeks()
	{
		return this.numWeeks;
	}
	public void setNumWeeks(int value)
	{
		this.numWeeks = value;
	}
	
	private int numDivisions;
	public int getNumDivisions()
	{
		return this.numDivisions;
	}
	public void setNumDivisions(int value)
	{
		this.numDivisions = value;
	}
	
	private String[] teamNames;
	public String[] getTeamNames()
	{
		return this.teamNames;
	}
	public void setTeamNames(String[] value)
	{
		this.teamNames = value;
	}
	
	private int numGames;
	public int getNumGames()
	{
		return this.numGames;
	}
	public void setNumGames(int value)
	{
		this.numGames = value;
	}
	
	private int massagedDataSize;
	public int getMassagedDataSize()
	{
		return this.massagedDataSize;
	}
	public void setMassagedDataSize(int value)
	{
		this.massagedDataSize = value;
	}
	
	private String[][] gameScores;
	public String[][] getGameScores()
	{
		return this.gameScores;
	}
	public void setGameScores(String[][] value)
	{
		this.gameScores = value;
	}
	
	private String[][] rawData;
	public String[][] getRawData()
	{
		return this.rawData;
	}
	public void setRawData(String[][] value)
	{
		this.rawData = value;
	}
	
	private String[][] teamStatsAsStrings;
	public String[][] getTeamStatsAsStrings()
	{
		return this.teamStatsAsStrings;
	}
	public void setTeamStatsAsStrings(String[][] value)
	{
		this.teamStatsAsStrings = value;
	}
	
	private float[][] teamStatsAsFloats;
	public float[][] getTeamStatsAsFloats()
	{
		return this.teamStatsAsFloats;
	}
	public void setTeamStatsAsFloats(float[][] value)
	{
		this.teamStatsAsFloats = value;
	}
	
	private String[][] leagueStats;
	public String[][] getLeagueStats()
	{
		return this.leagueStats;
	}
	public void setLeagueStats(String[][] value)
	{
		this.leagueStats = value;
	}
	
	
	

	
/*	private int LEAGUEWIDESTATCOUNT = 15;
	
	create a variable in here for every container in the main class
	pass the single instance of DataManager through to every function in TrackerRobot
	call the getter and setter method once per function, using temporary variables as the intermediaries
	
	//function start
	DataManager.getFinalTeamStats returns an empty double array of whatever
	
	DataManager.setFinalTeamStats(array[][]) takes a double array of whatever and sets those values into the backing store array*/

}
