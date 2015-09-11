package atm.stat.TrackerRobot;

import java.io.File;
import jxl.*; 

public class TrackerRobot {
	
	final static int teamDataSize = 8;

	public static void main(String[] args) {
		try 
		{
			//mac
			 //Workbook workbook = Workbook.getWorkbook(new File("/Users/alexmann/Developer/Fantasy_Stat_Tracking/inputFile_old.xls")); 	
			 
			 //windows
			 Workbook workbook = Workbook.getWorkbook(new File("C:\\Users\\Alex\\Documents\\GitHub\\Fantasy_Stat_Tracking\\inputFile_old.xls")); 	

			 
			 //getting number of teams
		     Sheet sheet = workbook.getSheet(0); 	     
		     Cell a2 = sheet.getCell(0,1); 
		     String teamCount = a2.getContents();
		     
		     System.out.println(teamCount);
		     
		     extractData(sheet, teamCount);
		     
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	//todo - finish extracting data
	private static void extractData(Sheet sheet, String teamCount) {
		int teamCountInt = Integer.parseInt(teamCount);
		int dataSize = teamCountInt * teamDataSize;
		
		String[] data = new String[dataSize];
		int pos = 0;
		
		for (int x = 0; x < teamCountInt; x++)
		{
			int col = 1;
			int row = 1;
			for (int y = 0; x < teamDataSize; x++)
			{
				Cell teamName = sheet.getCell(col, row);
			}
		}
	}

}
