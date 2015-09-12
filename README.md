# Fantasy_Stat_Tracking

This project is currently under active development. 

Todo:
Finish the three modules 
1) Data collection
2) Data massaging
3) Data delivery
4) ctrl+f "todo"
5) review code & this doc for leftovers


Stretch goal:
- Dynamically find the input file in the local directory to make the program platform agnostic
- Use jxl to write season-persistent stats back to the spreadsheet so that lifetime numbers are tracked
	- currently, there's no mechanism to separate season totals from lifetime totals. every 13 weeks,
	  totals need to be written to the spreadsheet and new season figures should be started
- Inter-divisional stats need to be implemented

--------------------
--------------------


working notes below:

[1] 		Lifetime W/L 
[# teams - 1]	 Matchup W/L 
[2]		Avg PF/PA (personal)
[2]		High/Low PF (personal)
[2]		High/Low PA (personal)
[1]		H/A W/L (personal)
[1]		H/A W/L (league)
[1]		Kept drafted 
[1]		Div record (personal)
[1]		10 highest 
[1]		10 lowest 
[1]		Div/Div W/L 
[2]		league total PF/PA 
[2]		season PF/PA
[2]		Avg margin of victory/defeat
[2]		highest margin of victory/defeat
[2]		lowest margin of victory/defeat
[2]		league avg margin of victory/defeat
[2]		league highest margein of victory/defeat
[2]		league lowest margin of victory/defeat
[1]		home team total points for season
[1]		away team total points for season
[1]		home team total points lifetime
[1]		away team total points lifetime


algorithm for massaging data:
iterate through one person's full historical stats, adding them to a new 2d array
calculate their stats, saving potential league relevant stats as you go
when the calculations are done, store the resulting numbers in another new 2d array 
go back to the extracted data, iterate through tht next person's full historical stats, adding them to the intermediate 2d array
calculate their stats, saving / overwriting potential league relevant stats as you go
when the calculations are done, store the resulting numbers in the next row of the final 2d array
repeat for teamCountInt times


ROW, COL
[ROW][COL] FOR 2D ARRAY