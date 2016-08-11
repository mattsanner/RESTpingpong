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
