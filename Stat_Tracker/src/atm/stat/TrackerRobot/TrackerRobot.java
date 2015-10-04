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
	static float currentWins = 0;
	static float currentLosses = 0;
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
	static float currentHighMarginVictory = 0;
	static float currentHighmarginDefeat = 0;
	static float currentLowMarginVictory = 0;
	static float currentLowMarginDefeat = 0;
	static float currentAverageMarginVictory = 0;
	static float currentAverageMarginDefeat = 0;
	static float currentTotalMarginVictory = 0;
	static float currentTotalMarginDefeat = 0;
	static float currentTotalPointsHome = 0;
	static float currentTotalPointsAway = 0;

	public static void main(String[] args) {
		try 
		{
			
			//getting the file from current directory? works on mac. need to test on windows
			String userDir = System.getProperty("user.dir");
			userDir = userDir.concat("/ACT_data.xls");
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
			
			String[] teamNames = new String[10];
			teamNames = getTeamNames(sheet);
						
			//LOGIC   
			String[][] data = extractData(sheet, teamCountInt, numWeeksInt);
			String[][] finalTeamSpecificDataAsStrings = massageData(data, teamCountInt, finalMassagedDataSize, numWeeksInt);
			printData(finalTeamSpecificDataAsStrings, teamNames);

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

	private static void printData(String[][] finalTeamSpecificDataAsStrings, String[] teamNames) throws FileNotFoundException, UnsupportedEncodingException {
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Results.txt"), "utf-8"))) 
		{
			int teamIndex = 0;
			
			for (int x = 0; x < teamNames.length; x++)
			{
				writer.write(teamNames[teamIndex]);
				writer.newLine();
				writer.write("Wins: " + finalTeamSpecificDataAsStrings[teamIndex][WINS]);
				writer.newLine();
				writer.write("Losses: " + finalTeamSpecificDataAsStrings[teamIndex][LOSSES]);
				writer.newLine();
				writer.write("Home winning pct: " + finalTeamSpecificDataAsStrings[teamIndex][HOMEWINPCT]);
				writer.newLine();
				writer.write("Away winning pct: " + finalTeamSpecificDataAsStrings[teamIndex][AWAYWINPCT]);
				writer.newLine();
				writer.write("Divisional record: " + finalTeamSpecificDataAsStrings[teamIndex][DIVRECORD]);
				writer.newLine();
				writer.write("Non divisional record: " + finalTeamSpecificDataAsStrings[teamIndex][NONDIVRECORD]);
				writer.newLine();
				writer.write("Total points for: " + finalTeamSpecificDataAsStrings[teamIndex][TOTALPOINTSFOR]);
				writer.newLine();
				writer.write("Total points against: " + finalTeamSpecificDataAsStrings[teamIndex][TOTALPOINTSAGAINST]);
				writer.newLine();
				writer.write("Average points for: " + finalTeamSpecificDataAsStrings[teamIndex][AVGPOINTSFOR]);
				writer.newLine();
				writer.write("Average points against: " + finalTeamSpecificDataAsStrings[teamIndex][AVGPOINTSAGAINST]);
				writer.newLine();
				writer.write("Highest points for: " + finalTeamSpecificDataAsStrings[teamIndex][HIGHPOINTSFOR]);
				writer.newLine();
				writer.write("Lowest points for: " + finalTeamSpecificDataAsStrings[teamIndex][LOWPOINTSFOR]);
				writer.newLine();
				writer.write("Highest points against: " + finalTeamSpecificDataAsStrings[teamIndex][HIGHPOINTSAGAINST]);
				writer.newLine();
				writer.write("Lowest points against: " + finalTeamSpecificDataAsStrings[teamIndex][LOWPOINTSAGAINST]);
				writer.newLine();
				writer.write("Total margin of victory: " + finalTeamSpecificDataAsStrings[teamIndex][TOTALMARGINVICTORY]);
				writer.newLine();
				writer.write("Total margin of defeat: " + finalTeamSpecificDataAsStrings[teamIndex][TOTALMARGINDEFEAT]);
				writer.newLine();
				writer.write("Average margin of victory: " + finalTeamSpecificDataAsStrings[teamIndex][AVGMARGINVICTORY]);
				writer.newLine();
				writer.write("Average margin of defeat: " + finalTeamSpecificDataAsStrings[teamIndex][AVGMARGINDEFEAT]);
				writer.newLine();
				writer.write("Highest margin of victory: " + finalTeamSpecificDataAsStrings[teamIndex][HIGHMARGINVICTORY]);
				writer.newLine();
				writer.write("Highest margin of defeat: " + finalTeamSpecificDataAsStrings[teamIndex][HIGHMARGINDEFEAT]);
				writer.newLine();
				writer.write("Lowest margin of victory: " + finalTeamSpecificDataAsStrings[teamIndex][HIGHMARGINVICTORY]);
				writer.newLine();
				writer.write("Lowest margin of defeat: " + finalTeamSpecificDataAsStrings[teamIndex][LOWMARGINDEFEAT]);
				writer.newLine();
				writer.write("Average home points: " + finalTeamSpecificDataAsStrings[teamIndex][AVGPOINTSHOME]);
				writer.newLine();
				writer.write("Average away points: " + finalTeamSpecificDataAsStrings[teamIndex][AVGPOINTSAWAY]);
				writer.newLine();
				writer.newLine();
				writer.newLine();
				teamIndex++;
			}
			
		} catch (Exception e)
		{
			System.out.println("out of printData");
			e.printStackTrace();
		}
	}

	
	
	
	
	//HELPER FUNCTIONS
	
	private static String[] getTeamNames(Sheet sheet) {
		String[] teamNames = new String[10];
		String teamNameString;
		int col = 0;
		int pos = 0;
		
		for (int row = 6; row < 16; row++)
		{
			Cell teamName = sheet.getCell(col, row);
			teamNameString = teamName.getContents();
			teamNames[pos] = teamNameString;
			pos++;
		}
		
		return teamNames;
	}
	
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
			float tempPointsFor = Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]);
			float tempPointsAgainst = Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			float tempMargin;
			
			
			currentTotalFor+= Float.parseFloat(tempData[currentWeek][TeamPointsForIndex]); 
			currentTotalAgainst+= Float.parseFloat(tempData[currentWeek][TeamPointsAgainstIndex]);
			
			//team wins and losses, home wins and losses, divisional wins and losses, margins of victory/defeat		
			if (tempData[currentWeek][TeamGameResultIndex].equals("W"))
			{	
				tempMargin = tempPointsFor - tempPointsAgainst;
				currentTotalMarginVictory+= tempMargin;
				
				if (finalTeamSpecificData[currentTeam][HIGHMARGINVICTORY] == 0)
				{
					finalTeamSpecificData[currentTeam][HIGHMARGINVICTORY] = tempMargin;
				} 
				else if (finalTeamSpecificData[currentTeam][HIGHMARGINVICTORY] < tempMargin)
				{
					finalTeamSpecificData[currentTeam][HIGHMARGINVICTORY] = tempMargin;
				}
				
				if (finalTeamSpecificData[currentTeam][LOWMARGINVICTORY] == 0)
				{
					finalTeamSpecificData[currentTeam][LOWMARGINVICTORY] = tempMargin;
				} 
				else if (finalTeamSpecificData[currentTeam][LOWMARGINVICTORY] > tempMargin)
				{
					finalTeamSpecificData[currentTeam][LOWMARGINVICTORY] = tempMargin;
				}
				
				if (tempData[currentWeek][TeamLocationIndex].equals("H"))
				{
					currentTotalPointsHome+= tempPointsFor;
					currentHomeWins++;
					currentHomeGames++;
				} 
				else if (tempData[currentWeek][TeamLocationIndex].equals("A"))
				{
					currentTotalPointsAway+= tempPointsFor;
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
				tempMargin = tempPointsAgainst - tempPointsFor;
				currentTotalMarginDefeat+= tempMargin;
				
				if (finalTeamSpecificData[currentTeam][HIGHMARGINDEFEAT] == 0)
				{
					finalTeamSpecificData[currentTeam][HIGHMARGINDEFEAT] = tempMargin;
				} 
				else if (finalTeamSpecificData[currentTeam][HIGHMARGINDEFEAT] < tempMargin)
				{
					finalTeamSpecificData[currentTeam][HIGHMARGINDEFEAT] = tempMargin;
				}
				
				if (finalTeamSpecificData[currentTeam][LOWMARGINDEFEAT] == 0)
				{
					finalTeamSpecificData[currentTeam][LOWMARGINDEFEAT] = tempMargin;
				} 
				else if (finalTeamSpecificData[currentTeam][LOWMARGINDEFEAT] > tempMargin)
				{
					finalTeamSpecificData[currentTeam][LOWMARGINDEFEAT] = tempMargin;
				}
				
				if (tempData[currentWeek][TeamLocationIndex].equals("H"))
				{
					currentTotalPointsHome+= tempPointsFor;
					currentHomeGames++;
				}
				else if (tempData[currentWeek][TeamLocationIndex].equals("A"))
				{
					currentTotalPointsAway+= tempPointsFor;
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
			else if (tempData[currentWeek][TeamGameResultIndex].equals("T"))
			{
				//ties are not implemented in this version
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
			
			//average margin of victory
			finalTeamSpecificData[currentTeam][AVGMARGINVICTORY] = currentTotalMarginVictory / finalTeamSpecificData[currentTeam][WINS];
			
			//average margin of defeat
			finalTeamSpecificData[currentTeam][AVGMARGINDEFEAT] = currentTotalMarginDefeat / finalTeamSpecificData[currentTeam][LOSSES];
			
			//total margin victory
			finalTeamSpecificData[currentTeam][TOTALMARGINVICTORY] = currentTotalMarginVictory;
			
			//total margin defeat
			finalTeamSpecificData[currentTeam][TOTALMARGINDEFEAT] = currentTotalMarginDefeat;
			
			//average points at home
			finalTeamSpecificData[currentTeam][AVGPOINTSHOME] = currentTotalPointsHome / currentHomeGames;
			
			//average points away
			finalTeamSpecificData[currentTeam][AVGPOINTSAWAY] = currentTotalPointsAway / currentAwayGames;
			
			
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
		currentHighMarginVictory = 0;
		currentHighmarginDefeat = 0;
		currentLowMarginVictory = 0;
		currentLowMarginDefeat = 0;
		currentTotalMarginVictory = 0;
		currentTotalMarginDefeat = 0;
		currentWins = 0;
		currentLosses = 0;
		currentTotalPointsHome = 0;
		currentTotalPointsAway = 0;
		currentHomeGames = 0;
		currentAwayGames = 0;
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
	final static int LOWMARGINVICTORY = 22;
	final static int LOWMARGINDEFEAT = 23;
	final static int TEAMNAME = 24;

}
