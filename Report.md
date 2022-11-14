# Clonewar

| Developers | Version of document | last update |
| :---------: | :-----------------:| :----------: |
| Ramaroson Rakotomihamina Johan && Tellier Quentin | 0.1 | 14/11/2022|

## About 
Clonewar is a web-app that allow you to analyze and detect clones among multiple java project. 
It works using artifacts that can be build by giving a jar-archive. From here the application will index all the instructions in bytecode of the project. Then you can analyze several artifacts knowing if there is clone between them.

## Architecture
Clonewar is divide in two sub-applications : a back-end for persistence and computation and a front-end for user interface.


### I. Back-end
In this part, all the computation and logic will be implemented. Such as the Karp-Rabin algorithm which will be used for detecting clones among projects. 

The back-end part has 2 main connexions : 
1. With the outside, the web.
2. With our database.

So basically, the first one is alimented by classes which represents controller that represents methods in our api rest, so we will use dto to map json object to our java application. 
And the second one represent the requests and interaction with our database. 

### II. Front-end


## Technologies 

|       Back        | Front | Mapping persistence | Database | UI |
|:-----------------:|:-----:|:------:| :--------:|:-------:| 
| Spring 6 Reactive | React | Jakarta | Sqlite | Bulma

