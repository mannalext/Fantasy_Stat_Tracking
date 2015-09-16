package atm.stat.TrackerRobot;

import java.io.File;
import jxl.*; 

public class TrackerRobot {
	
	final static int SingleTeamDataSize = 8;
	final static int TeamNameIndex = 0;
	final static int TeamLocationIndex = 1;
	final static int TeamPointsForIndex = 2;
	final static int TeamPointsAgainstIndex = 3;
	final static int TeamGameResultIndex = 4;
	final static int TeamDivisionIndex = 5;
	final static int TeamOpponentIndex = 6;
	final static int TeamDivisionalGameIndex = 7;
	
	static int MassagedDataSizeBeforeTeamCountAdded = 30; //todo - likely to change
	
	//STATS STATS STATS 
	//todo - inter-divisional stats not currently implemented
    static int HomeTeamSeasonWins = 0;
    static int AwayTeamSeasonWins = 0;
    static int[] HighestSeasonScores = new int[10];
    static int[] LowestSeasonScores = new int[10];
    static int NumberOfDivisions;
    static int SeasonTotalPointsScored = 0;
    static int HomeTeamTotalPointsScoredSeason = 0;
    static int AwayTeamTotalPointsScoredSeason = 0;
    static int AverageMarginOfVictorySeason = 0;
    static int HighestMarginOfVictorySeason = 0;
    static int LowestMarginOfVictorySeason = 0;
    
    //NOTE: the implementation of these lifetime stats will change upon completion of a stretch goal
    static int HomeTeamLifetimeWins = 0;
    static int AwayTeamLifetimeWins = 0;
    static int[] HighestLifetimeScores = new int[10];
    static int[] LowestLifetimeScores = new int[10];
    static int LeagueTotalPointsScored = 0;
    static int HomeTeamTotalPointsScoredLifetime = 0;
    static int AwayTeamTotalPointsScoredLifetime = 0;
    static int AverageMarginOfVictoryLifetime = 0;
    static int HighestMarginOfVictoryLifetime = 0;
    static int LowestMarginOfVictoryLifetime = 0;
    
    //STATS STATS STATS

	public static void main(String[] args) {
		try 
		{
			//mac
			 Workbook workbook = Workbook.getWorkbook(new File("/Users/alexmann/Developer/Fantasy_Stat_Tracking/inputFile_old.xls")); 	
			 
			 //windows
			 //Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\Alex\\Documents\\GitHub\\Fantasy_Stat_Tracking\\inputFile_old.xls")); 	

		     Sheet sheet = workbook.getSheet(0); 	  
		     
		     
		     Cell a2 = sheet.getCell(0,1); 
		     String teamCount = a2.getContents();
		     int teamCountInt = Integer.parseInt(teamCount);
		     int finalMassagedDataSize = teamCountInt - 1 + MassagedDataSizeBeforeTeamCountAdded;
		     
		     Cell b1 = sheet.getCell(1, 1);
		     String numWeeks = b1.getContents();
		     int numWeeksInt = Integer.parseInt(numWeeks);
		     
		     Cell a3 = sheet.getCell(0, 3);
		     String numDivisions = a3.getContents();
		     int numDivisionsInt = Integer.parseInt(numDivisions);
		     NumberOfDivisions = numDivisionsInt;
		     
		     System.out.println("team count: " + teamCount);
		     System.out.println("number of weeks: " + numWeeks);
		     System.out.println("number of divisions: " + numDivisions);
		     
		     //LOGIC   
		     String[][] data = extractData(sheet, teamCountInt, numWeeksInt);
		     String[][] finalTeamSpecificData = massageData(data, teamCountInt, finalMassagedDataSize, numWeeksInt);
		     
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private static String[][] extractData(Sheet sheet, int teamCountInt, int numWeeksInt) {
		
		int dataSize = teamCountInt * SingleTeamDataSize;
		
		String[][] data = new String[numWeeksInt][dataSize];
		int pos = 0;
		int col = 2;
		int row = 1;
		int week = 0;
		
		for (int y = 0; y < numWeeksInt; y++)
		{
			for (int x = 0; x < dataSize; x++)
			{
				Cell datum = sheet.getCell(col, row);
				String datumString = datum.getContents();
				data[week][pos] = datumString;
				col++;
				pos++;
			}
			week++;
			row++;
			col = 2;
			pos = 0;
		}
	
		return data;
	}

	private static String[][] massageData(String[][] data, int teamCountInt, int finalMassagedDataSize, int numWeeksInt) {
		int currentHigh = 0;
		int currentLow = 0;
		int currentTotalFor = 0;
		int currentTotalAgainst = 0;
		int currentWins = 0;
		int currentLosses = 0;
		
		String[][] finalTeamSpecificData = new String[teamCountInt][finalMassagedDataSize]; //this will be the result of this function
		String[][] tempData = new String[numWeeksInt][finalMassagedDataSize]; //this is the temp container that will hold each person's full historical stats, one person at a time
		
		int increment = 0;
		int currentWeek = 0;
		
		for (int x = 0; x < teamCountInt; x++)
		{
			tempData = extractOneTeam(data, numWeeksInt, increment);
			for (int y = 0; y < numWeeksInt; y++) 
			{
				currentTotalFor+= Integer.parseInt(tempData[currentWeek][TeamPointsForIndex]); //PARSING FLOATS, NOT INTS
			}
			increment++;
			currentWeek++;
		}
				
		return finalTeamSpecificData;
	}

	private static String[][] extractOneTeam(String[][] data, int numWeeksInt, int increment) {
		int team = 0;
		int row = 0;
		int col = 0;
		int offset = SingleTeamDataSize * increment;
		String[][] container = new String[numWeeksInt][SingleTeamDataSize];
		
		for (int y = 0; y < numWeeksInt; y++)
		{
			for (int x = 0; x < SingleTeamDataSize; x++)
			{
				container[row][col] = data[team][x + offset];
				col++;
			}
			col = 0;
			team++;
			row++;
		}
		return container;
	}
}
