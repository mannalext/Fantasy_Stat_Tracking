# Fantasy_Stat_Tracking

This project is currently under active development. 

TODO:
- League-Wide stat tracking
- included in league-wide, but deserves a callout: matchup tracking
- Season-over-season stats, "lifetime of the league" tracking
- Tie tracking
- Determine a "pretty" way to output the data to .txt
	- Then, use a StringBuilder to make that string and print it
- Inter-divisional stats need to be implemented (East / West win pct against each other)
- highest/lowest scores BREAK when there are duplicates. easy to fix with a bool
- lowest winning score, highest losing score


STRETCH GOALS:
- refactor such that there are SIGNIFICANTLY less static variables
- write data to a spreadsheet so Hannah can make excel infographics
- use the 'fantasy' repo on git to scrape ESPN for numbers instead of using data entry
- implement a gui for help options, potentially output options
- handle when people join/leave the league



--Thoughts as I build it--

12/30/15
Updating stat sheets to reflect the entire regular season. Getting thoughts in order for offseason additions to the app. Chief addition to the project plan is the
ability to track highest losing score and lowest winning score

10/20/15
added dynasty input file to the project. fixed some small bugs that were discovered as a result of the dynasty data

10/5/15 second commit
implemented top and bottom 5 league scoring, along with rudimentary printing. thinking that next will be all the regular league-wide stats that are easier to track. list these in the input file

10/5/15
beginning the groundwork for league-wide stats. reorganized this doc. putting focus on assembling league-wide stats right now, then moving to pretty printing

9/30/15
useful link for exporting an executable
http://www.wikihow.com/Create-an-Executable-File-from-Eclipse

9/22/15
- in order to track margins of victory / defeat, calculate them inside the main WIN or LOSS statements
- tracking all single team stats in rows on the input file now, temporarily ofc
- moving forward, track all single team stats and print them. that's V1.0
- after V1.0, think about stats across seasons / for the full league
- full league stats first
- stats across seasons second

9/15/15
DESIGN CHANGE PROPOSAL:
the current app will only do one season's worth of stats. it will export these to an excel file AND the .txt
a second app will be written which will read in seasonal stats and massage them into lifetime stats
note: this change was made with an unclear head. reevaluate when the time comes




















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