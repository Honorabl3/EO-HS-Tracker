=========================================================
================= EO Highscore Tracker! =================
=========================================================
================= MESSAGE Feb. 20, 2024 =================

This java project/jar was written by 'Endless-Online Recharged' player "China".. The program is written without any malicious intent, e.g. this tool is virus/malware free. Generally speaking it's important not to run/execute any .exe, .jar -- and other variant forms -- to keep your computer/environment always secure. Normally when distributing this tool it should be downloaded as the raw .java source files, and the user themselves compile the code so there is transparency knowing they can trust the source. 


This program is supposed to act as a tool to help my fellow xp-grinders track their xp/hr progress.


================= REQUIREMENTS =================

1. Install Java (skip this if you already have Java JRE8+ installed).
	- https://www.java.com/en/download/


================= HOW TO USE =================

1. Run debug.bat
2. Wait for data to be pulled from eodash website.
3. View data and changes overtime.


================= CHANGELOG =================

- [V0.4] Apr. 3rd, 2024
	- [Added] Line graph for each entry that simulates activity based off data of exp change data.
	- [Added] Modifications to the 'Settings' tab.
		- [Feature] Settings now save inside the settings.ser file on trigger of the 'Apply button', simply delete 'settings.ser' to reset back to default settings.
		- [Added] 'Reset Default' button was added to revert all data to default settings.
		- [Added] 'Graph Node Spacing' text field, the horizontal distance will ultimately be capped by the horizontal size of the overall window.

- [V0.3] Mar. 25, 2024
	- [Added] 'Settings' tab to add customization.
		- [Feature] Option to change *RGB* grid color variables.
		- [Feature] Option to change font, and font color.
		- [Feature] Option to change default pull-size.
		- [Fix] Special cases where XP/HR was displaying wrong information.
- [V0.2] Feb. 22, 2024
	- [Added] 2D Window/Application with useful buttons.
		- [Feature] New Formatting to scores list.
		- [Added] Specific Player Tracking feature with a corresponding text field and 'Add Player' button.
		- [Added] 'Manual Reset' button for dumping old data and restarting process without exiting/restarting the tool.
		- [Added] 'Changelog' Button to briefly show changes from inside the tool.

- [V0.1] Feb. 16, 2024
	- [Feature] Tracker presents data on Console / Command Prompt style window.
	- [Fix] Now correctly accesses eodash api to pull data.
	- [Fix] Tracker broke functionality because of eodash DDOS protection changes.


================= FUTURE PLANS =================

- [Feature] "Name" tracking: Additionally pull/display/track data on a specific player that isn't at the top of the highscores.


================= ISSUES =================

I am aware there are currently a lot of bugs.. Perhaps future modifications I will fix them, but the motivation to do so isn't on my list of priorities (gotta get that xp first).


- [Annoyance] The tool pulls data from the eodash.com website, if eodash service/utility website becomes unavailable, the highscore tracker will cease to pull data rendering the tool useless.
- [Bug] Running the process the first time won't display the highscores.
	- This is normal and you just need to wait the 2.5 minutes for the list to pull data again.
- [Bug] Random Thrown error in between highscore entries for some names.
	- This bug occurs when the ranking positions of players on the highscores list 'pass up' other peoples EXP count.. This doesn't seem to impact performance (I think) and is easily fixed when restarting the process frequently.


There are probably more bugs. If you happen to find bugs/ideas/suggestions please don't hesitate to reach out so I may develop this tool further.


================= DEPENDENCIES =================

Running the Debug.bat file executes the 'EO-HS-Tracker.jar' file, which uses two other .jar files as libraries/dependencies. All dependencies come already-provided with this tool. The explanation of the other .jar dependencies are as followed:
	- jsoup-1.16.1.jar: This library allows ease-of-use web access to pull data from the eodash website.
	- gson-2.10.1.jar: This library builds off the jsoup library to provide easy JSON-format parsing.




Written by player "China" 2024©, all rights reserved.