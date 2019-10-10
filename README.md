# relationalDatabase

A database developed using Java and based off of the MySQL relational database.
This program accepts terminal commands and was created as part of a 3-person student team.

Running
--------
To parse commands in file, edit the SURLY.bat to read: java -jar SURLY.jar "<filelocation>" (e.g. java -jar SURLY.jar "C:\testfile.txt")

Commands
---------
 
## Command Structure and Examples
`command relation_name {command-specific arguments}`

* RELATION - Adds a new relation to the database.
	** Usage: `RELATION relationname (<attrib format>);`
	** Example: `Example: RELATION COURSE (CNUM CHAR 8, TITLE CHAR 30, CREDITS NUM 4);`

* INSERT - Inserts a tuple with the given data into the given relation.
	** Usage: `INSERT relationname <tuple>;`
	** Example: `INSERT COURSE CSCI241 'DATA STRUCTURES' 4;`

* PRINT - Prints any relations supplied by the user as an ASCII table.
	** Usage: `PRINT {RELATION LIST}`
	** Example: `PRINT STUDENT, COURSE`

* SELECT - Displays a temporary relation containing all tuples that meet the WHERE clause conditions
	** Usage: `<temprelname> = SELECT relname WHERE <list of conditions>`
	** Example: `C = SELECT COURSE;`
	** Example: `T1 = SELECT OFFERING WHERE CID = CSCI241 AND SECTION > 27922;`
	** Example: `PRE = SELECT PREREQ WHERE CNBR = CSCI241 or CNBR = CSCI145 and PNBR != CSCI141;`

* PROJECT - Displays a temporary relation containing specified attributes of a relation
	** Usage: `<temprelname> = PROJECT <list of attributes> FROM <relation>;");`
	** Example: `P = PROJECT CREDITS, CNUM FROM COURSE;`

* JOIN - Return combined tuples from the 2 mentioned relations that satisfy specified condition(s)
	** Usage: `temprelationname = JOIN relationAname, relationBname ON <join condition>;`
	** Example: `J = JOIN COURSE, PREREQ ON CNUM = PNBR;`

* DELETE - Delete tuple(s) that satisfy specifed condtiion from given relation
	** Usage: `DELETE relationname WHERE <conditions>`
	** Example: `DELETE OFFERING WHERE CID = CSCI241 and SECTION > 27922;`

* DESTROY - Removes a given relation from the database.
	**  Usage: `DESTROY <relationName>`
	** Example: `DESTROY COURSE, OFFERINGBYINSTRBYCOURSE;`

* HELP - Provides help information for SURLY commands.
	** Usage: `HELP <command>`
	** Example: `HELP RELATION`

* FILE - Processes a file containing lines of commands into the database.
	** Usage: `FILE <filepath>`
	** Example: `FILE C:\\TEST-input.txt`

* EXIT - Exits the SURLY program.
