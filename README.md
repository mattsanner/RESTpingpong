# RESTpingpong
Rewrite of main code to use MySQL driver for DB use, making the application light weight.

# Installation

Download source code and build using mvn ('mvn clean package' in pingpong directory).
Must have Tomcat 8 and MySQL installed on machine (through aptitude installer).
Copy built WAR file to Tomcat's webapp directory.

There is a relatively untested method for setting up database tables on start up.
To create the tables by hand I used namespace pingpong and tables with create statements:

CREATE TABLE pingpong.players (
    firstname VARCHAR(15),
    lastname VARCHAR(20),
    losses int,
    pointsagainst double,
    pointsfor double,
    wins int,
    PRIMARY KEY (firstname, lastname)
);

CREATE TABLE pingpong.matches (
    player1 VARCHAR(36),
    player2 VARCHAR(36),
    p1score double,
    p1wins int,
    p2score double,
    p2wins int,
    PRIMARY KEY (player1, player2)
);

CREATE TABLE pingpong.matchlist (
    matchKey int,
    time timestamp,
    p1score int,
    p2score int,
    player1 VARCHAR(36),
    player2 VARCHAR(36),
    PRIMARY KEY (key, time)
);

# Usage

Web app used to create players, record matches, and display the records of players, different matchups, and recent matches.
To start application go to your tomcat directory (for XPX: /home/pi/tomcat8) and run the catalina.sh script with 'run' parameter: "/home/pi/tomcat8/bin/catalina.sh run"

The above script should startup tomcat that automatically deploys the war in the webapps directory. The terminal should fill with startup logging then mention the server start up in XX seconds which indicates it is good to go.

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
