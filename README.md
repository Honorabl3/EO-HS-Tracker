# Endless Online High Score Tracker
<small>Powered by ~~EODash.com~~ eor-api.exile-studios.com</small>
A tool designed to provide relatively accurate XP/HR predictions based on a sample size of data. In addition to the XP/HR feature, it also includes a graph showcasing continuous activity. The tool allows for specific tracking of character names and offers a wide range of options to personalize to your needs.
 


![alt text](https://github.com/Honorabl3/EO-HS-Tracker/blob/main/EO-HS-Tracker/images/picture1.png?raw=true)

![alt text](https://github.com/Honorabl3/EO-HS-Tracker/blob/main/EO-HS-Tracker/images/picture2.png?raw=true)

![alt text](https://github.com/Honorabl3/EO-HS-Tracker/blob/main/EO-HS-Tracker/images/picture3.png?raw=true)

# How To Use
 1. Run debug.bat
 2. ~~Wait for data to be pulled from eodash website.~~
 3. View data and changes overtime.

# Installation
 1. Install Java (skip this if you already have Java JRE8+ installed).
	 - https://www.java.com/en/download/
 2. [Download latest EO Highscore Tracker release](https://github.com/Honorabl3/EO-HS-Tracker/releases/tag/v0.4)
 3. Extract the zip file contents from the downloaded release


# Changelog
 - [V0.5.1] Sep 22, 2025
	- [Fix] Monster Log reading new chatline entries which were changed by EO update.
	- [Fix] Lazy hotfix to remove EODash pull request.
 - [V0.5] Sep. 10, 2024
	- [Added] API access to random EO data from eor-api.exile-studios.com.
	- [Added] Monster Log
		- [Feature] Live Progress data of monsters you kill.
		- [Feature] Live progress of picked up or harvested items.
		- [Feature] Monster info data when clicking on image of monster.
	- [Fix] GUI formatting issues in some panels.
	- [Added] Draw Graph Inactivity setting to keep graph lines from being removed from inactivity.
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


# Dependencies

Running the Debug.bat file executes the 'EO-HS-Tracker.jar' file, which uses two other .jar files as libraries/dependencies. All dependencies come already-provided with this tool. The explanation of the other .jar dependencies are as followed:
	- jsoup-1.16.1.jar: This library allows ease-of-use web access to pull data from the eor-api.exile-studios.com website.
	- gson-2.10.1.jar: This library builds off the jsoup library to provide easy JSON-format parsing.



Written by player "China" 2025©, all rights reserved.
