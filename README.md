# Fantasy_Stat_Tracking

This project is currently under active development. 

UPDATE AS OF SEPTEMBER 2017:
The next step for this project is to make a new branch and restart development.
It’s way too messy to be built upon. It needs a total refactor.

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







-----BEGIN NOTES FOR MASSIVE REFACTOR------

1. completely changing how the spreadsheet is organized

1st sheet is metadata. num teams, num weeks, num season?
remaining n sheets one per team
EACH TEAM GETS ITS OWN SHEET FOR 100% OF ITS DATA


2. create 2 new data structures, splitting DataManager up
     - one for per team stats
     - one for league wide stats

rather than building the stats in one giant mess, build them per team
this data structure contains 100% of the stats relating to that team
give it accessor functions to allow easy calculating of league-wide stats

*important*
each time a week's worth of data is brought into the data structure, 100% of the stats are calculated and stored. we calculate n times where n is the number of weeks, rather than 1 time with the full dataset


extractData() becomes a new function which extracts raw data FOR ONE TEAM, and stores it neatly into some kind of bridge data structure. 

massageData() takes that bridge data structure (again, holding a single team's raw data) and runs stat calculations on it, storing those stats into the new data structure from step 2


4. printData() retains its old functionality. requires a cleanup refactor though


brainstorming for what these new data structures need to look like

team data
- this new data structure has its own print function, removing TrackerRobot's print function
- has one entry point, "trackWeek()" which takes a week's worth of data for that one team as an argument (raw spreadsheet data), and massages it into the team specific and league wide stats I have right now, and whatever others I add


league data
- has its own print function as well
- all of the team data structures have access to this one because they will need to make calls to it during their trackWeek() work



anatomy of a call to main()

1. get metadata from sheet 1
2. initialize n team data structures into an array, where n is the number of teams found in the metadata
3. initialize league wide data structure
4. double for loop calling team[n].trackWeek(x)
     -trackWeek will look at team n's week x. it will extract that raw data. it         will operate on it, updating the stats for that one team and any relevant stats for the league as a whole.


we HAVE to calculate stats each time we pull a week. do NOT do it any other way. calculate each time a week is extracted from the spreadsheet


-----END NOTES FOR MASSIVE REFACTOR-------






























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