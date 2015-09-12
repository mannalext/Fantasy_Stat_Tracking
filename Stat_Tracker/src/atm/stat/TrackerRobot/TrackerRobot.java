package atm.stat.TrackerRobot;

import java.io.File;
import jxl.*; 

public class TrackerRobot {
	
	final static int singleTeamDataSize = 8;

	public static void main(String[] args) {
		try 
		{
			//mac
			 Workbook workbook = Workbook.getWorkbook(new File("/Users/alexmann/Developer/Fantasy_Stat_Tracking/inputFile_old.xls")); 	
			 
			 //windows
			 //Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\Alex\\Documents\\GitHub\\Fantasy_Stat_Tracking\\inputFile_old.xls")); 	

			 
			 //getting number of teams
		     Sheet sheet = workbook.getSheet(0); 	     
		     Cell a2 = sheet.getCell(0,1); 
		     String teamCount = a2.getContents();
		     
		     System.out.println("team count: " + teamCount);
		     
		     String[] data = extractData(sheet, teamCount);
		     
		     String[] fixedData = massageData(data);
		     
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private static String[] extractData(Sheet sheet, String teamCount) {
		int teamCountInt = Integer.parseInt(teamCount);
		int dataSize = teamCountInt * singleTeamDataSize;
		
		String[] data = new String[dataSize];
		int pos = 0;
		int col = 1;
		int row = 1;
		
		for (int x = 0; x < dataSize; x++)
		{
			Cell datum = sheet.getCell(col, row);
			String datumString = datum.getContents();
			data[pos] = datumString;
			col++;
			pos++;
		}
		
		return data;
	}

	private static String[] massageData(String[] data) {
		String[] fixedData = new String[data.length]; //the length of fixedData might not need to be the same as data. keep that in mind
		
		//loop X times, where X is number of teams
		//store each individual datum in a container variable denoting what it is (name, points for, win/los, etc)
		//calculate stats, store them in the new array 
		//return
		
		return fixedData;
	}
}
