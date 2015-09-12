package atm.stat.TrackerRobot;

import java.io.File;
import jxl.*; 

public class TrackerRobot {
	
	final static int SingleTeamDataSize = 8;
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
			 //Workbook workbook = Workbook.getWorkbook(new File("/Users/alexmann/Developer/Fantasy_Stat_Tracking/inputFile_old.xls")); 	
			 
			 //windows
			 Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\Alex\\Documents\\GitHub\\Fantasy_Stat_Tracking\\inputFile_old.xls")); 	

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
		     
		        
		     String[][] data = extractData(sheet, teamCountInt, numWeeksInt);
		     String[][] fixedData = massageData(data, teamCountInt, finalMassagedDataSize, numWeeksInt);
		     
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
		
		String[][] finalData = new String[teamCountInt][finalMassagedDataSize]; //this will be the result of this function
		String[][] tempData = new String[numWeeksInt][SingleTeamDataSize]; //this is the temp container that will hold each person's full historical stats, one person at a time
		
		tempData = extractOneTeam(data, numWeeksInt);
		
		//tempData now holds one team's lifetime data. build framework for massaging that data, and make sure that when you go back to pull more data you're pulling the next team (offset)
		
		return finalData;
	}

	private static String[][] extractOneTeam(String[][] data, int numWeeksInt) {
		int offset = 0;
		int row = 0;
		int col = 0;
		String[][] container = new String[numWeeksInt][SingleTeamDataSize];
		
		for (int y = 0; y < numWeeksInt; y++)
		{
			for (int x = 0; x < SingleTeamDataSize; x++)
			{
				container[row][col] = data[offset][x];
				col++;
			}
			col = 0;
			offset++;
			row++;
		}
		return container;
	}
}
