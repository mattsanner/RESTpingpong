# RESTpingpong
In progress application to record and track pingpong scores using REST services and a cassandra database

# Installation

Download source code and build using mvn ('mvn clean package' in pingpong directory).
Must have Tomcat 8 and the latest Cassandra 2 installed on machine.
Copy built WAR file to Tomcat's webapp directory.
Must start cassandra before deploying the webapp.

There is a relatively untested method for setting up database tables on start up.
To create the tables by hand I used namespace pingpong and tables with create statements:

CREATE TABLE pingpong.players (
    firstname text,
    lastname text,
    losses int,
    pointsagainst double,
    pointsfor double,
    wins int,
    PRIMARY KEY (firstname, lastname)
);

CREATE TABLE pingpong.matches (
    player1 text,
    player2 text,
    p1score double,
    p1wins int,
    p2score double,
    p2wins int,
    PRIMARY KEY (player1, player2)
);

CREATE TABLE pingpong.matchlist (
    key int,
    time timestamp,
    p1score int,
    p2score int,
    player1 text,
    player2 text,
    PRIMARY KEY (key, time)
) WITH CLUSTERING ORDER BY (time ASC);

# Usage

Web app used to create players, record matches, and display the records of players, different matchups, and recent matches.

To start application go to your tomcat directory (for XPX: /home/pi/tomcat8) and run the catalina.sh script with 'run' parameter: "/home/pi/tomcat8/bin/catalina.sh run"

The above script should startup tomcat that automatically deploys the war in the webapps directory. The terminal should fill with startup logging then mention the server start up in XX seconds which indicates it is good to go.

Create a player by appending '/create_player?firstName=Matt&lastName=Sanner' to the given local address for the app, replacing the player's first and last name with Matt and Sanner respectively. This can also be done manually with MySQL with a simple INSERT statement.

# Contributing

Currently a closed group contributing to the repo. Feel free to clone and make your own changes.
Contact me if you would like to be involved in some way.

# History

Started as a research project to cover some of the technologies I will be using at work.

# Credits

Author: mattsanner
Help From: djbamba

# License:
Currently - None
