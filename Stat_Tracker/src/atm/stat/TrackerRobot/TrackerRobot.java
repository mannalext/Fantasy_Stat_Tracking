package atm.stat.TrackerRobot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.PriorityQueue;

import jxl.*; 

public class TrackerRobot {
	
	static DataManager data;

	public static void main(String[] args) {
		try 
		{
			//getting the data file from current directory
			//this will be generalized when I build the UI. file will be selectable there
			String userDir = System.getProperty("user.dir");
			userDir = userDir.concat("/Dynasty_data.xls");
			Workbook workbook = Workbook.getWorkbook(new File(userDir));	
			Sheet sheet = workbook.getSheet(0); 
			
		
			//DataManager object initialization
			data = new DataManager();
			
			
			//basic setup data
			Cell a2 = sheet.getCell(0,1); 
			String teamCount = a2.getContents();
			int teamCountInt = Integer.parseInt(teamCount);
			data.setTeamCount(teamCountInt);			

			Cell b1 = sheet.getCell(1, 1);
			String numWeeks = b1.getContents();
			int numWeeksInt = Integer.parseInt(numWeeks);
			data.setNumWeeks(numWeeksInt);

			Cell a3 = sheet.getCell(0, 3);
			String numDivisions = a3.getContents();
			int numDivisionsInt = Integer.parseInt(numDivisions);
			data.setNumDivisions(numDivisionsInt);
			
			String[] teamNames = new String[10];
			teamNames = extractTeamNames(sheet);
			data.setTeamNames(teamNames);
			
			int totalGameCount = numWeeksInt * teamCountInt;
			data.setNumGames(totalGameCount);
			
			int finalMassagedDataSize = teamCountInt - 1 + DATASIZEBEFORETEAMCOUNT;
			data.setMassagedDataSize(finalMassagedDataSize);
			//end basic setup data
						
			
			//LOGIC CALLS 
			String[][] tempRawData = extractData(sheet); //remove the return value from extractData and store it straight into data.rawData
			data.setRawData(tempRawData);
			massageData();
			printData();
			//String[][] finalTeamSpecificDataAsStrings = massageData(data, finalMassagedDataSize);
			

		} catch (Exception e)
		{
			System.out.println("out of main");
			e.printStackTrace();
			//System.out.println(e.printStackTrace());
		}
	}

	private static String[][] extractData(Sheet sheet) {
		int teamCount = data.getTeamCount();
		int numWeeks = data.getNumWeeks();
		int dataSize = teamCount * SINGLETEAMDATASIZE;
		String[][] rawData = new String[numWeeks][dataSize];
		int pos = 0;
		int col = 2;
		int row = 1;
		int week = 0;

		try 
		{
			for (int y = 0; y < numWeeks; y++)
			{
				for (int x = 0; x < dataSize; x++)
				{
					Cell datum = sheet.getCell(col, row);
					String datumString = datum.getContents();
					rawData[week][pos] = datumString;
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
		return rawData;
	}

	private static void massageData() {
		int teamCount = data.getTeamCount();
		int numWeeks = data.getNumWeeks();
		int numGames = data.getNumGames();
		int massagedDataSize = data.getMassagedDataSize();
		String[][] rawData = data.getRawData();
		float[][] teamStatsAsFloats = new float[teamCount][massagedDataSize];
		String[][] oneTeam = new String[teamCount][massagedDataSize];
		String[][] gameScores = new String[2][numGames];
		String[][] leagueWideStats = new String[2][LEAGUEWIDESTATCOUNT];
		int initialCloseGame = 1000;
		int initialBlowout = 0;
		leagueWideStats[0][CLOSESTGAME] = Integer.toString(initialCloseGame);
		leagueWideStats[0][BIGGESTBLOWOUT] = Integer.toString(initialBlowout);
		data.setLeagueStats(leagueWideStats);
		int currentTeam = 0;
		int currentWeek = 0;
		int gameIndex = 0;

		
		float[][] finalTeamSpecificData = new float[teamCount][massagedDataSize]; //this will be the result of this function

		try 
		{
			for (int x = 0; x < teamCount; x++)
			{
				oneTeam = extractOneTeam(currentTeam);
				for (int y = 0; y < numWeeks; y++) 
				{
					massageTeamWeekIntoTeamStats(oneTeam, finalTeamSpecificData, currentTeam, gameIndex, currentWeek, gameScores);
					gameIndex++;
					currentWeek++;
				}
				currentTeam++;
				currentWeek = 0;
				assembleLeagueStats(oneTeam, currentTeam);
				clearTemporaryVariables();
			}
			
			data.setGameScores(gameScores);
			data.setTeamStatsAsFloats(finalTeamSpecificData);
			
			highestAndLowestScores();
			
			convertFloatArrayToString();
			System.out.println("debug");

		} catch (Exception e)
		{
			System.out.println("out of massageData");
			e.printStackTrace();
		}
		
	}

	private static void printData() throws FileNotFoundException, UnsupportedEncodingException {
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Results.txt"), "utf-8"))) 
		{
			String[] teamNames = data.getTeamNames();
			String[][] teamStatsAsStrings = data.getTeamStatsAsStrings();
			String[][] leagueWideStats = data.getLeagueStats();
			int teamIndex = 0;
			
			for (int x = 0; x < teamNames.length; x++)
			{
				writer.write(teamNames[teamIndex]);
				writer.newLine();
				writer.write("Wins: " + teamStatsAsStrings[teamIndex][WINS]);
				writer.newLine();
				writer.write("Losses: " + teamStatsAsStrings[teamIndex][LOSSES]);
				writer.newLine();
				writer.write("Home winning pct: " + teamStatsAsStrings[teamIndex][HOMEWINPCT]);
				writer.newLine();
				writer.write("Away winning pct: " + teamStatsAsStrings[teamIndex][AWAYWINPCT]);
				writer.newLine();
				writer.write("Divisional record: " + teamStatsAsStrings[teamIndex][DIVRECORD]);
				writer.newLine();
				writer.write("Non divisional record: " + teamStatsAsStrings[teamIndex][NONDIVRECORD]);
				writer.newLine();
				writer.write("Total points for: " + teamStatsAsStrings[teamIndex][TOTALPOINTSFOR]);
				writer.newLine();
				writer.write("Total points against: " + teamStatsAsStrings[teamIndex][TOTALPOINTSAGAINST]);
				writer.newLine();
				writer.write("Average points for: " + teamStatsAsStrings[teamIndex][AVGPOINTSFOR]);
				writer.newLine();
				writer.write("Average points against: " + teamStatsAsStrings[teamIndex][AVGPOINTSAGAINST]);
				writer.newLine();
				writer.write("Highest points for: " + teamStatsAsStrings[teamIndex][HIGHPOINTSFOR]);
				writer.newLine();
				writer.write("Lowest points for: " + teamStatsAsStrings[teamIndex][LOWPOINTSFOR]);
				writer.newLine();
				writer.write("Highest points against: " + teamStatsAsStrings[teamIndex][HIGHPOINTSAGAINST]);
				writer.newLine();
				writer.write("Lowest points against: " + teamStatsAsStrings[teamIndex][LOWPOINTSAGAINST]);
				writer.newLine();
				writer.write("Total margin of victory: " + teamStatsAsStrings[teamIndex][TOTALMARGINVICTORY]);
				writer.newLine();
				writer.write("Total margin of defeat: " + teamStatsAsStrings[teamIndex][TOTALMARGINDEFEAT]);
				writer.newLine();
				writer.write("Average margin of victory: " + teamStatsAsStrings[teamIndex][AVGMARGINVICTORY]);
				writer.newLine();
				writer.write("Average margin of defeat: " + teamStatsAsStrings[teamIndex][AVGMARGINDEFEAT]);
				writer.newLine();
				writer.write("Highest margin of victory: " + teamStatsAsStrings[teamIndex][HIGHMARGINVICTORY]);
				writer.newLine();
				writer.write("Highest margin of defeat: " + teamStatsAsStrings[teamIndex][HIGHMARGINDEFEAT]);
				writer.newLine();
				writer.write("Lowest margin of victory: " + teamStatsAsStrings[teamIndex][LOWMARGINVICTORY]);
				writer.newLine();
				writer.write("Lowest margin of defeat: " + teamStatsAsStrings[teamIndex][LOWMARGINDEFEAT]);
				writer.newLine();
				writer.write("Total point differential: " + teamStatsAsStrings[teamIndex][POINTDIFFERENTIAL]);
				writer.newLine();
				writer.write("Average home points: " + teamStatsAsStrings[teamIndex][AVGPOINTSHOME]);
				writer.newLine();
				writer.write("Average away points: " + teamStatsAsStrings[teamIndex][AVGPOINTSAWAY]);
				writer.newLine();
				writer.newLine();
				teamIndex++;
			}
			writer.write("BEGINNING LEAGUE-WIDE STATS");
			writer.newLine();
			writer.write("Top 5 Single Week Scores");
			writer.newLine();
			writer.write("1. " + leagueWideStats[0][HIGHESTSCORE] + "\t(" + leagueWideStats[1][HIGHESTSCORE] + ")");
			writer.newLine();
			writer.write("2. " + leagueWideStats[0][SECONDHIGHESTSCORE] + "\t(" + leagueWideStats[1][SECONDHIGHESTSCORE] + ")");
			writer.newLine();
			writer.write("3. " + leagueWideStats[0][THIRDHIGHESTSCORE] + "\t(" + leagueWideStats[1][THIRDHIGHESTSCORE] + ")");
			writer.newLine();
			writer.write("4. " + leagueWideStats[0][FOURTHHIGHESTSCORE] + "\t(" + leagueWideStats[1][FOURTHHIGHESTSCORE] + ")");
			writer.newLine();
			writer.write("5. " + leagueWideStats[0][FIFTHHIGHESTSCORE] + "\t(" + leagueWideStats[1][FIFTHHIGHESTSCORE] + ")");
			writer.newLine();
			writer.newLine();
			writer.write("Bottom 5 Single Week Scores");
			writer.newLine();
			writer.write("1. " + leagueWideStats[0][LOWESTSCORE] + "\t(" + leagueWideStats[1][LOWESTSCORE] + ")");
			writer.newLine();
			writer.write("2. " + leagueWideStats[0][SECONDLOWESTSCORE] + "\t(" + leagueWideStats[1][SECONDLOWESTSCORE] + ")");
			writer.newLine();
			writer.write("3. " + leagueWideStats[0][THIRDLOWESTSCORE] + "\t(" + leagueWideStats[1][THIRDLOWESTSCORE] + ")");
			writer.newLine();
			writer.write("4. " + leagueWideStats[0][FOURTHLOWESTSCORE] + "\t(" + leagueWideStats[1][FOURTHLOWESTSCORE] + ")");
			writer.newLine();
			writer.write("5. " + leagueWideStats[0][FIFTHLOWESTSCORE] + "\t(" + leagueWideStats[1][FIFTHLOWESTSCORE] + ")");
			writer.newLine();
			writer.newLine();
			writer.write("The Biggest Blowout");
			writer.newLine();
			String[] blowout = leagueWideStats[1][BIGGESTBLOWOUT].split(" ");
			writer.write(blowout[0] + " beat " + blowout[1] + " by a whopping " + leagueWideStats[0][BIGGESTBLOWOUT] + " in week " + blowout[3]);
			writer.newLine();
			writer.write("The Closest Game");
			writer.newLine();
			String[] close = leagueWideStats[1][CLOSESTGAME].split(" ");
			writer.write(close[0] + " beat " + close[1] + " by the narrow margin of " + leagueWideStats[0][CLOSESTGAME] + " in week " + close[3]);
			
		} catch (Exception e)
		{
			System.out.println("out of printData");
			e.printStackTrace();
		}
	}


	//HELPER FUNCTIONS BELOW
	
	private static String[] extractTeamNames(Sheet sheet) {
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
	
	private static String[][] extractOneTeam(int increment) {
		int team = 0;
		int row = 0;
		int col = 0;
		int offset = SINGLETEAMDATASIZE * increment;
		int numWeeks = data.getNumWeeks();
		String[][] rawData = data.getRawData();
		String[][] container = new String[numWeeks][SINGLETEAMDATASIZE];

		try 
		{
			for (int y = 0; y < numWeeks; y++)
			{
				for (int x = 0; x < SINGLETEAMDATASIZE; x++)
				{
					container[row][col] = rawData[team][x + offset];
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

	private static void massageTeamWeekIntoTeamStats(String[][] tempData, float[][] finalTeamSpecificData, int currentTeam, int gameIndex, int currentWeek, String[][] gameScores) {
		
		try 
		{
			int numWeeks = data.getNumWeeks();
			float tempPointsFor = Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]);
			float tempPointsAgainst = Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			float tempMargin;
			
			currentTotalFor+= Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]); 
			currentTotalAgainst+= Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			
			//team wins and losses, home wins and losses, divisional wins and losses, margins of victory/defeat		
			if (tempData[currentWeek][TEAMGAMERESULTINDEX].equals("W"))
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
				
				if (tempData[currentWeek][TEAMLOCATIONINDEX].equals("H"))
				{
					currentTotalPointsHome+= tempPointsFor;
					currentHomeWins++;
					currentHomeGames++;
				} 
				else if (tempData[currentWeek][TEAMLOCATIONINDEX].equals("A"))
				{
					currentTotalPointsAway+= tempPointsFor;
					currentAwayWins++;
					currentAwayGames++;
				}
				
				if (tempData[currentWeek][TEAMDIVISIONALGAMEINDEX].equals("Y"))
				{
					currentDivisionalWins++;
					currentDivisionalGames++;
				}
				else if (tempData[currentWeek][TEAMDIVISIONALGAMEINDEX].equals("N"))
				{
					currentNonDivisionalWins++;
					currentNonDivisionalGames++;
				}
				finalTeamSpecificData[currentTeam][WINS]++;
			}
			else if (tempData[currentWeek][TEAMGAMERESULTINDEX].equals("L"))
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
				
				if (tempData[currentWeek][TEAMLOCATIONINDEX].equals("H"))
				{
					currentTotalPointsHome+= tempPointsFor;
					currentHomeGames++;
				}
				else if (tempData[currentWeek][TEAMLOCATIONINDEX].equals("A"))
				{
					currentTotalPointsAway+= tempPointsFor;
					currentAwayGames++;
				}
				if (tempData[currentWeek][TEAMDIVISIONALGAMEINDEX].equals("Y"))
				{
					currentDivisionalGames++;
				}
				else if (tempData[currentWeek][TEAMDIVISIONALGAMEINDEX].equals("N"))
				{
					currentNonDivisionalGames++;
				}
				finalTeamSpecificData[currentTeam][LOSSES]++;
			}
			else if (tempData[currentWeek][TEAMGAMERESULTINDEX].equals("T"))
			{
				//ties are not implemented in this version
			}
			
			//team average points for
			finalTeamSpecificData[currentTeam][AVGPOINTSFOR] = currentTotalFor / numWeeks;
			
			//team average points against
			finalTeamSpecificData[currentTeam][AVGPOINTSAGAINST] = currentTotalAgainst / numWeeks;
			
			//team high points for
			if (finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] == 0)
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]);
			} 
			else if (finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] < Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]))
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]);
			}
			
			//team low points for
			if (finalTeamSpecificData[currentTeam][LOWPOINTSFOR] == 0)
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]);
			} 
			else if (finalTeamSpecificData[currentTeam][LOWPOINTSFOR] > Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]))
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSFOR] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSFORINDEX]);
			}
			
			//team high points against
			if (finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] == 0)
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			} 
			else if (finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] < Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]))
			{
				finalTeamSpecificData[currentTeam][HIGHPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			}
			
			//team low points against
			if (finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] == 0)
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			} 
			else if (finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] > Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]))
			{
				finalTeamSpecificData[currentTeam][LOWPOINTSAGAINST] = Float.parseFloat(tempData[currentWeek][TEAMPOINTSAGAINSTINDEX]);
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
			
			//point differential
			finalTeamSpecificData[currentTeam][POINTDIFFERENTIAL] = currentTotalMarginVictory - currentTotalMarginDefeat;
			
			
			//BEGIN LEAGUE WIDE STATS
			
			//this assembles a list of every individual team score, with the owner attributed in the second row of the array
			gameScores[0][gameIndex] = tempData[currentWeek][TEAMPOINTSFORINDEX];
			gameScores[1][gameIndex] = tempData[currentWeek][TEAMNAMEINDEX];
			
			
		} catch (Exception e)
		{
			System.out.println("out of massageTeamWeekIntoTeamStats");
			e.printStackTrace();
		}
		
	}

	private static void assembleLeagueStats(String[][] oneTeam, int currentTeam) {

		try
		{
		//every time this function gets called, tempData will contain the entire data of one team
		String[][] allTeams = data.getRawData();
		String[][] leagueWideStats = data.getLeagueStats();		
		int numWeeks = data.getNumWeeks();
		int currentWeek = 0;
		float differential;
		String player;
		String opponent;
		String result;
		
		for (int x = 0; x < numWeeks; x++)
		{
			differential = Float.parseFloat(oneTeam[currentWeek][TEAMPOINTSFORINDEX]) - Float.parseFloat(oneTeam[currentWeek][TEAMPOINTSAGAINSTINDEX]);
			
			
			if (differential > Float.parseFloat(leagueWideStats[0][BIGGESTBLOWOUT]))
			{
				leagueWideStats[0][BIGGESTBLOWOUT] = Float.toString(differential);
				
				player = oneTeam[currentWeek][TEAMNAMEINDEX];
				opponent = oneTeam[currentWeek][TEAMOPPONENTINDEX];
				result = oneTeam[currentWeek][TEAMGAMERESULTINDEX];
				
				leagueWideStats[1][BIGGESTBLOWOUT] = player + " " + opponent + " " + result + " " + Integer.toString(currentWeek+1);
			} 
			else if ((differential < Float.parseFloat(leagueWideStats[0][CLOSESTGAME])) && differential > 0)
			{
				leagueWideStats[0][CLOSESTGAME] = Float.toString(differential);
				
				player = oneTeam[currentWeek][TEAMNAMEINDEX];
				opponent = oneTeam[currentWeek][TEAMOPPONENTINDEX];
				result = oneTeam[currentWeek][TEAMGAMERESULTINDEX];
				
				leagueWideStats[1][CLOSESTGAME] = player + " " + opponent + " " + result + " " + Integer.toString(currentWeek+1);
			}			

			data.setLeagueStats(leagueWideStats);
			currentWeek++;
		}
		
		System.out.println("debug");
		
		} catch (Exception e)
		{
			System.out.println("out of assembleLeagueStats");
			e.printStackTrace();
		}
		
	
	}
	
	private static void highestAndLowestScores() {
		try 
		{
			String[][] leagueWideStats = data.getLeagueStats();
			String[][] gameScores = data.getGameScores();
			float high1 = 0;
			float high2 = 0;
			float high3 = 0;
			float high4 = 0;
			float high5 = 0;
			float low1 = 0;
			float low2 = 0;
			float low3 = 0;
			float low4 = 0;
			float low5 = 0;
			
			high1 = Float.parseFloat(gameScores[0][0]);
			low1 = Float.parseFloat(gameScores[0][0]);
			
			for (int x = 1; x < gameScores[0].length; x++)
			{
				//5 highest scores
				if (Float.parseFloat(gameScores[0][x]) > high1)
				{
					high5 = high4;
					high4 = high3;
					high3 = high2;
					high2 = high1;
					high1 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) > high2)
				{
					high5 = high4;
					high4 = high3;
					high3 = high2;
					high2 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) > high3)
				{
					high5 = high4;
					high4 = high3;
					high3 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) > high4)
				{
					high5 = high4;
					high4 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) > high5)
				{
					high5 = Float.parseFloat(gameScores[0][x]);
				}
				
				//5 lowest scores
				if (Float.parseFloat(gameScores[0][x]) < low1)
				{
					low5 = low4;
					low4 = low3;
					low3 = low2;
					low2 = low1;
					low1 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) < low2)
				{
					low5 = low4;
					low4 = low3;
					low3 = low2;
					low2 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) < low3)
				{
					low5 = low4;
					low4 = low3;
					low3 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) < low4)
				{
					low5 = low4;
					low4 = Float.parseFloat(gameScores[0][x]);
				} else if (Float.parseFloat(gameScores[0][x]) < low5)
				{
					low5 = Float.parseFloat(gameScores[0][x]);
				}
			}
			
			for (int y = 0; y < gameScores[0].length; y++)
			{
				String string = gameScores[0][y];
				if (high1 == Float.parseFloat(string)) 
				{
					leagueWideStats[0][HIGHESTSCORE] = gameScores[0][y];
					leagueWideStats[1][HIGHESTSCORE] = gameScores[1][y];
				} else if (high2 == Float.parseFloat(string))
				{
					leagueWideStats[0][SECONDHIGHESTSCORE] = gameScores[0][y];
					leagueWideStats[1][SECONDHIGHESTSCORE] = gameScores[1][y];
				} else if (high3 == Float.parseFloat(string))
				{
					leagueWideStats[0][THIRDHIGHESTSCORE] = gameScores[0][y];
					leagueWideStats[1][THIRDHIGHESTSCORE] = gameScores[1][y];
				} else if (high4 == Float.parseFloat(string))
				{
					leagueWideStats[0][FOURTHHIGHESTSCORE] = gameScores[0][y];
					leagueWideStats[1][FOURTHHIGHESTSCORE] = gameScores[1][y];
				} else if (high5 == Float.parseFloat(string))
				{
					leagueWideStats[0][FIFTHHIGHESTSCORE] = gameScores[0][y];
					leagueWideStats[1][FIFTHHIGHESTSCORE] = gameScores[1][y];
				} else if (low1 == Float.parseFloat(string))
				{
					leagueWideStats[0][LOWESTSCORE] = gameScores[0][y];
					leagueWideStats[1][LOWESTSCORE] = gameScores[1][y];
				} else if (low2 == Float.parseFloat(string))
				{
					leagueWideStats[0][SECONDLOWESTSCORE] = gameScores[0][y];
					leagueWideStats[1][SECONDLOWESTSCORE] = gameScores[1][y];
				} else if (low3 == Float.parseFloat(string))
				{
					leagueWideStats[0][THIRDLOWESTSCORE] = gameScores[0][y];
					leagueWideStats[1][THIRDLOWESTSCORE] = gameScores[1][y];
				} else if (low4 == Float.parseFloat(string))
				{
					leagueWideStats[0][FOURTHLOWESTSCORE] = gameScores[0][y];
					leagueWideStats[1][FOURTHLOWESTSCORE] = gameScores[1][y];
				} else if (low5 == Float.parseFloat(string))
				{
					leagueWideStats[0][FIFTHLOWESTSCORE] = gameScores[0][y];
					leagueWideStats[1][FIFTHLOWESTSCORE] = gameScores[1][y];
				}
			}
		
			data.setLeagueStats(leagueWideStats);
			System.out.println("debug");
			
		} catch (Exception e)
		{
			System.out.println("out of highestAndLowestScores");
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

	private static void convertFloatArrayToString() {
		int teamCount = data.getTeamCount();
		int massagedDataSize = data.getMassagedDataSize();
		float[][] finalTeamSpecificData = data.getTeamStatsAsFloats();
		String[][] finalTeamSpecificDataAsStrings = new String[teamCount][massagedDataSize];
		
		for (int x = 0; x < teamCount; x++)
		{
			for (int y = 0; y < massagedDataSize; y++)
			{
				finalTeamSpecificDataAsStrings[x][y] = Float.toString(finalTeamSpecificData[x][y]);
			}
		}
	
		data.setTeamStatsAsStrings(finalTeamSpecificDataAsStrings);
	}
	
	
	//team and league wide stat counts
	final static int DATASIZEBEFORETEAMCOUNT = 30;
	final static int LEAGUEWIDESTATCOUNT = 15;
	
	//individual team statistic array indices (for finalTeamSpecificStats arrays)
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
	final static int POINTDIFFERENTIAL = 24;
	final static int TEAMNAME = 31;
	
	//league wide statistic array indices (for leagueWideStats arrays)
	final static int HIGHESTSCORE = 0;
	final static int SECONDHIGHESTSCORE = 1;
	final static int THIRDHIGHESTSCORE = 2;
	final static int FOURTHHIGHESTSCORE = 3;
	final static int FIFTHHIGHESTSCORE = 4;
	final static int LOWESTSCORE = 5;
	final static int SECONDLOWESTSCORE = 6;
	final static int THIRDLOWESTSCORE = 7;
	final static int FOURTHLOWESTSCORE = 8;
	final static int FIFTHLOWESTSCORE = 9;
	final static int BIGGESTBLOWOUT = 10;
	final static int CLOSESTGAME = 11;
	final static int BESTPOINTDIFFERENTIAL = 12;
	final static int WORSTPOINTDIFFERENTIAL = 12;
	
	
	//individual team statistic EXTRACTION array indices
	final static int TEAMNAMEINDEX = 0;
	final static int TEAMLOCATIONINDEX = 1;
	final static int TEAMPOINTSFORINDEX = 2;
	final static int TEAMPOINTSAGAINSTINDEX = 3;
	final static int TEAMDIVISIONINDEX = 4;
	final static int TEAMGAMERESULTINDEX = 5;
	final static int TEAMOPPONENTINDEX = 6;
	final static int TEAMDIVISIONALGAMEINDEX = 7;
	final static int SINGLETEAMDATASIZE = 8;

	
	//temp containers for data massaging (single teams)
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

}
