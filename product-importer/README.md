# Mobile-catalogue importer

## Before running 

### Neo4j

1. A running instance of neo4j is required either locally or in a docker container.
2. *APOC plugin* needs to be installed on the graph db.
   1. The URL where the latest jar can be obtained is under the section
      *Manual Installation: Download latest release*
   
   ### For Graph db running on a docker instance
   2. Instructions to install the apoc plugin in a docker container can be found under the section 
      *Using APOC with the Neo4j Docker image*
   3. The securities for the dbms procedures should be unrestricted. To do this:
      docker run \
     -e NEO4J_dbms_security_procedures_unrestricted=apoc.\\\* \
      NEO4J_dbms.allow_format_migration=true \
     -v $PWD/plugins:/plugins \
     --publish=7474:7474 --publish=7687:7687 \
     --volume=$HOME/neo4j/data:/data \
     --volume=$HOME/neo4j/logs:/logs \
     neo4j:3.2.0-enterprise

   ### For local instance
   4. Stop neo4j instance using the command _neo4j stop_. 
   5. Create a plugins directory inside the neo4j home directory (as a sibling to other directories like conf), 
      copy the downloaded apoc jar to this location. 
   6. Start the neo4j instance using the command _neo4j start_.
   7. In the neo4j.conf file which can be found inside the neo4j home directory the following line needs
      to be set :  _dbms.security.procedures.unrestricted=apoc.*_
      
      
   8. *_Most Importantly_* set the password of the graph db in the application.yml file inside the 
       *src/main/resources* directory of the application irrespective of whether the graph db is run inside the docker
       or locally.

## Overview of what the program does

1. The current neo4j database contents including the data, indexes, and constraints will be wiped-out 
   entirely.
2. The contents of the mobile_catalogue.json will be loaded into the database.
3. New indexes and constraints would be created.
4. The status of the application can be followed from the logs inside _logs/catalogue_data_importer.log_.
5. If everything goes well the application will boot properly.
6. Else an _exception would be thrown_ indicating the error encountered.

## Optionally generate a gradle wrapper

This is to run the application locally. To generate the gradle wrapper, go to the directory of the application and enter:
gradle wrapper. This should generate a gradle wrapper. Using a gradle wrapper is highly recommended than using a local gradle installation.

## Running the application

### From the IDE
   Run the application either as a java application or a spring boot application from Eclipse/Intellij.
### From the command line
   If gradle wrapper is present then the application can be run as gradlew bootRun for Windows env and ./gradlew bootRun
   for *nix systems.
   
## After a successful startup:

1. Once the application boots up go to the http://localhost:7474 to view the instance of the neo4j 
   instance to which the data was loaded.
2. You will get an overview of the number of nodes, their labels, and the relations that were created.
3. You can run basic queries to check the sanity of the data. You can also click on any of the labels
   (like Promotions, Options etc) which will generate a basic query displaying the information.
4. For checking if the indexes and constraints were created properly run the query :Schema. This will 
   list all the indexes and constraints that were created.
