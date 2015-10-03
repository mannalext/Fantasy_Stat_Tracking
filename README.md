# Fantasy_Stat_Tracking

This project is currently under active development. 

Todo:
Finish the three modules 
1) Data collection
2) Data massaging
3) Data delivery
4) ctrl+f "todo"
5) review code & this doc for leftovers
6) use the followed python repo on git to scrape espn instead of using data entry
7) gui for help, options, etc
8) implement functionality to handle people joining and leaving the league
9) implement tracking in the case of Ties
10) use a StringBuilder to to .txt output in printData
11) "total margin" as a stat. totalMarginVictory - totalMarginDefeat


Stretch goal:
- Dynamically find the input file in the local directory to make the program platform agnostic
- Use jxl to write season-persistent stats back to the spreadsheet so that lifetime numbers are tracked
    - write another app to consolidate lifetime stats by combining each season
- Inter-divisional stats need to be implemented
- implement a gui for help options, potentially output options, and practice lol

9/15/15
DESIGN CHANGE PROPOSAL:
the current app will only do one season's worth of stats. it will export these to an excel file AND the .txt
a second app will be written which will read in seasonal stats and massage them into lifetime stats
note: this change was made with an unclear head. reevaluate when the time comes


9/22/15
- in order to track margins of victory / defeat, calculate them inside the main WIN or LOSS statements
- tracking all single team stats in rows on the input file now, temporarily ofc
- moving forward, track all single team stats and print them. that's V1.0
- after V1.0, think about stats across seasons / for the full league
- full league stats first
- stats across seasons second

9/30/15
useful link for exporting an executable
http://www.wikihow.com/Create-an-Executable-File-from-Eclipse













EVERYTHING BELOW IS DEPRECATED
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