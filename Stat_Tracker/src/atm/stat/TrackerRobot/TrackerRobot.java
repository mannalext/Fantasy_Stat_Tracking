package atm.stat.TrackerRobot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import jxl.*; 

public class TrackerRobot {

	final static int SingleTeamDataSize = 8;
	final static int TeamNameIndex = 0;
	final static int TeamLocationIndex = 1;
	final static int TeamPointsForIndex = 2;
	final static int TeamPointsAgainstIndex = 3;
	final static int TeamDivisionIndex = 4;
	final static int TeamGameResultIndex = 5;
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
	
	//individual team stats, these are temporary and always changing
	//static float currentHigh = 0;
	//static float currentLow = 0;
	static float currentTotalFor = 0;
	static float currentTotalAgainst = 0;
	//static float currentWins = 0;
	//static float currentLosses = 0;
	static float currentHomeWins = 0;
	static float currentHomeGames = 0;
	static float currentAwayWins = 0;
	static float currentAwayGames = 0;
	static float currentHomeTotal = 0;
	static float currentAwayTotal = 0;
	static float currentDivisionalWins = 0;
	static float currentDivisionalGames = 0;
	static float currentNonDivisionalWins = 0;
	static float currentNonDivisionalGames = 0;

	public static void main(String[] args) {
		try 
		{
			
			//getting the file from current directory? works on mac. need to test on windows
			String userDir = System.getProperty("user.dir");
			userDir = userDir.concat("/inputFile_old.xls");
			System.out.println(userDir);
			Workbook workbook = Workbook.getWorkbook(new File(userDir));

			//mac
			//Workbook workbook = Workbook.getWorkbook(new File("/Users/alexmann/Developer/Fantasy_Stat_Tracking/inputFile_old.xls")); 	

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
			String[][] finalTeamSpecificDataAsStrings = massageData(data, teamCountInt, finalMassagedDataSize, numWeeksInt);
			printData(finalTeamSpecificDataAsStrings);

		} catch (Exception e)
		{
			System.out.println("out of main");
			e.printStackTrace();
			//System.out.println(e.printStackTrace());
		}
	}

	private static String[][] extractData(Sheet sheet, int teamCountInt, int numWeeksInt) {

		int dataSize = teamCountInt * SingleTeamDataSize;

		String[][] data = new String[numWeeksInt][dataSize];
		int pos = 0;
		int col = 2;
		int row = 1;
		int week = 0;

		try 
		{
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
			
		} catch (Exception e)
		{
			System.out.println("out of extractData");
			e.printStackTrace();
		}
		return data;
	}

	private static String[][] massageData(String[][] data, int teamCountInt, int finalMassagedDataSize, int numWeeksInt) {

		float[][] finalTeamSpecificData = new float[teamCountInt][finalMassagedDataSize]; //this will be the result of this function
		String[][] tempData = new String[numWeeksInt][finalMassagedDataSize]; //this is the temp container that will hold each person's full historical stats, one person at a time

		int increment = 0;
		int currentWeek = 0;

		try 
		{
			for (int x = 0; x < teamCountInt; x++)
			{
				tempData = extractOneTeam(data, numWeeksInt, increment);
				for (int y = 0; y < numWeeksInt; y++) 
				{
					massageTeamWeekIntoTeamStats(tempData, finalTeamSpecificData, numWeeksInt, currentWeek, increment); //passing increment as currentTeam index
					massageTeamWeekIntoLeagueStats(tempData, finalTeamSpecificData, numWeeksInt, currentWeek, increment);
					currentWeek++;
				}
				increment++;
				currentWeek = 0;
				clearTemporaryVariables();
			}
	
		} catch (Exception e)
		{
			System.out.println("out of massageData");
			e.printStackTrace();
		}
		
		String[][] finalTeamSpecificDataAsStrings = convertFloatArrayToString(finalTeamSpecificData, teamCountInt, finalMassagedDataSize);
		
		return finalTeamSpecificDataAsStrings;
		
	}

	private static void printData(String[][] finalTeamSpecificDataAsStrings) throws FileNotFoundException, UnsupportedEncodingException {
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Results.txt"), "utf-8"))) 
		{
			writer.write("Team 1 total points for: " + finalTeamSpecificDataAsStrings[0][TOTALPOINTSFOR]);
			writer.newLine();
			writer.write("Team 2 total points for: " + finalTeamSpecificDataAsStrings[1][TOTALPOINTSFOR]);
			writer.newLine();
			writer.write("Team 3 total points for: " + finalTeamSpecificDataAsStrings[2][TOTALPOINTSFOR]);
			writer.newLine();
			writer.write("Team 4 total points for: " + finalTeamSpecificDataAsStrings[3][TOTALPOINTSFOR]);

		} catch (Exception e)
		{
			System.out.println("out of printData");
			e.printStackTrace();
		}
	}

	
	
	
	//BEGIN HELPER VARIABLES
	
	private static String[][] extractOneTeam(String[][] data, int numWeeksInt, int increment) {
		int team = 0;
		int row = 0;
		int col = 0;
		int offset = SingleTeamDataSize * increment;
		String[][] container = new String[numWeeksInt][SingleTeamDataSize];

		try 
		{
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
		} catch (Exception e)
		{
			System.out.println("out of extractOneTeam");
			e.printStackTrace();
		}
		
		return container;
	}

	private static void massageTeamWeekIntoTeamStats(String[][] tempData, float[][] finalTeamSpecificData, int numWeeksInt, int currentWeek, int currentTeam) {
		
		try 
		{
			currentTotalFor+= Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]); 
			currentTotalAgainst+= Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			
			//team wins and losses			
			if (tempData[currentWeek][TeamGameResultIndex].equals("W"))
			{
				if (tempData[currentWeek][TeamLocationIndex].equals("H"))
				{
					currentHomeWins++;
					currentHomeGames++;
				} 
				else if (tempData[currentWeek][TeamLocationIndex].equals("A"))
				{
					currentAwayWins++;
					currentAwayGames++;
				}
				
				if (tempData[currentWeek][TeamDivisionalGameIndex].equals("Y"))
				{
					currentDivisionalWins++;
					currentDivisionalGames++;
				}
				else if (tempData[currentWeek][TeamDivisionalGameIndex].equals("N"))
				{
					currentNonDivisionalWins++;
					currentNonDivisionalGames++;
				}
				finalTeamSpecificData[currentTeam][WINS]++;
			}
			else if (tempData[currentWeek][TeamGameResultIndex].equals("L"))
			{
				if (tempData[currentWeek][TeamLocationIndex].equals("H"))
				{
					currentHomeGames++;
				}
				else if (tempData[currentWeek][TeamLocationIndex].equals("A"))
				{
					currentAwayGames++;
				}
				if (tempData[currentWeek][TeamDivisionalGameIndex].equals("Y"))
				{
					currentDivisionalGames++;
				}
				else if (tempData[currentWeek][TeamDivisionalGameIndex].equals("N"))
				{
					currentNonDivisionalGames++;
				}
				finalTeamSpecificData[currentTeam][LOSSES]++;
			}
			
			//team average points for
			finalTeamSpecificData[currentTeam][AVGPOINTSFOR] = currentTotalFor / numWeeksInt;
			
			//team average points against
			finalTeamSpecificData[currentTeam][AVGPOINTSAGAINST] = currentTotalAgainst / numWeeksInt;
			
			//team high points for
			if (finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] == 0)
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]);
			} 
			else if (finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] < Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]))
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]);
			}
			
			//team low points for
			if (finalTeamSpecificData[currentTeam][LOWPOINTSFOR] == 0)
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]);
			} 
			else if (finalTeamSpecificData[currentTeam][LOWPOINTSFOR] > Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]))
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]);
			}
			
			//team high points against
			if (finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] == 0)
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			} 
			else if (finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] < Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]))
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			}
			
			//team low points against
			if (finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] == 0)
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			} 
			else if (finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] > Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]))
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			}
			
			//team total points against
			finalTeamSpecificData[currentTeam][TOTALPOINTSAGAINST] = currentTotalAgainst;
			
			//team total points for
			finalTeamSpecificData[currentTeam][TOTALPOINTSFOR] = currentTotalFor;
			
			//team home pct
			finalTeamSpecificData[currentTeam][HOMEWINPCT] = currentHomeWins / currentHomeGames;
			
			//team away pct
			finalTeamSpecificData[currentTeam][AWAYWINPCT] = currentAwayWins / currentAwayGames;
			
			//team divisional pct
			finalTeamSpecificData[currentTeam][DIVRECORD] = currentDivisionalWins / currentDivisionalGames;
			
			//team non divisional pct
			finalTeamSpecificData[currentTeam][NONDIVRECORD] = currentNonDivisionalWins / currentNonDivisionalGames;
			
			//team total margin of victory
			
			
		} catch (Exception e)
		{
			System.out.println("out of massageTeamWeekIntoTeamStats");
			e.printStackTrace();
		}
		
	}

	private static void massageTeamWeekIntoLeagueStats(String[][] tempData, float[][] finalTeamSpecificData, int numWeeksInt, int currentWeek, int currentTeam) {

		try 
		{
			
		} catch (Exception e)
		{
			System.out.println("out of massageTeamWeekIntoLeagueStats");
			e.printStackTrace();
		}
	}
	
	private static void clearTemporaryVariables() {
		//currentHigh = 0;
		//currentLow = 0;
		currentTotalFor = 0;
		currentTotalAgainst = 0;
		currentHomeWins = 0;
		currentAwayWins = 0;
		currentHomeTotal = 0;
		currentAwayTotal = 0;
		currentDivisionalWins = 0;
		currentDivisionalGames = 0;
		currentNonDivisionalWins = 0;
		currentNonDivisionalGames = 0;
		//currentWins = 0;
		//currentLosses = 0;
	}

	private static String[][] convertFloatArrayToString(float[][] finalTeamSpecificData, int teamCountInt, int finalMassagedDataSize) {
		String[][] finalTeamSpecificDataAsStrings = new String[teamCountInt][finalMassagedDataSize];
		
		for (int x = 0; x < teamCountInt; x++)
		{
			for (int y = 0; y < finalMassagedDataSize; y++)
			{
				finalTeamSpecificDataAsStrings[x][y] = Float.toString(finalTeamSpecificData[x][y]);
			}
		}
		
		return finalTeamSpecificDataAsStrings;
	}
	
	final static int WINS = 0;
	final static int LOSSES = 1;
	final static int AVGPOINTSFOR = 2;
	final static int HIGHPOINTSFOR = 3;
	final static int LOWPOINTSFOR = 4;
	final static int TOTALPOINTSFOR = 5;
	final static int AVGPOINTSAGAINST = 6;
	final static int HIGHPOINTSAGAINST = 7;
	final static int LOWPOINTSAGAINST = 8;
	final static int TOTALPOINTSAGAINST = 9;
	final static int HOMEWINPCT = 10;
	final static int AWAYWINPCT = 11;
	final static int DIVRECORD = 12;
	final static int AVGMARGINVICTORY = 13;
	final static int AVGMARGINDEFEAT = 14;
	final static int HIGHMARGINVICTORY = 15;
	final static int HIGHMARGINDEFEAT = 16;
	final static int AVGPOINTSHOME = 17;
	final static int AVGPOINTSAWAY = 18;
	final static int NONDIVRECORD = 19;
	final static int TOTALMARGINVICTORY = 20;
	final static int TOTALMARGINDEFEAT = 21;
	final static int TEAMNAME = 22;

}
