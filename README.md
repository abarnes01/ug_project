# Avoiding Shoulder Surfing via Graphical Passwords

## Information about this repository

- <code>Software Artefacts</code> contains any artefacts mentioned in project material, such as the Interim Report or Dissertation.
- Any external dependencies I have downloaded online for the SQL database will be in the <code>SQL Dependencies</code> folder.
- Any other dependencies are in the <code>Ext Dependencies</code>
- The runnable JAR file is contained in the <code>Executable</code> folder.
- The project code and relevant files can be found inside the <code>gp-evaluation-software</code> folder.

_Please note: Commits made on the GitLab website will show the user as A Barnes. All commits pushed from a local repository will show the user as abarnes01._

# Installation Instructions

You need to have a machine running Windows, Linux or MacOS. It is recommended to use MacOS, as the software was created on this operating system.

First, clone this repository to your machine.

### MySQL server setup

To use the suite, you need to have a MySQL server installed, up and running. Please follow the MySQL documentation to install MySQL on your machine [here](https://dev.mysql.com/doc/refman/8.0/en/installing.html).

Start up the MySQL server before running. Have your MySQL [address](https://dev.mysql.com/doc/refman/8.0/en/installing.html), name and password on hand. It should be something like the following:
- url = "jdbc:mysql://localhost:3306/"
- user = "root"
- password = ""

## Method 1 - Running the executable

Run the runnable jar file <code>Executable/gpsuite.jar</code>. 

This requires Java Runtime version <code>58.0</code> and a compatible JVM. Make sure the executable is given permission to run.

## Method 2 - Running off eclipse

_Please note: Only use these steps if Method 1 does not work._

<ol>
  <li>Download and open [Eclipse](https://www.eclipse.org/downloads/).</li>
  <li>Import Existing Project into Workspace; select <code>ab1049/gp-evaluation-software</code></li>
  <li>All libraries and dependencies will have to be imported again to the build path. JRE 14 is needed.</li>
  <li>You can run DatabaseSetup.java in the IDE. Make sure the argument <code>-XstartOnFirstThread</code> is unchecked on MacOS.</li>
</ol>

_For any queries, please email me at [ab1049@student.le.ac.uk](ab1049@student.le.ac.uk)_
