#VeeGee's Polls API

Hello there!

This is a little backend application I wrote in late January 2021 as part of a challenge!

The scenario for this API is helping a local community organize their Polling Sessions. We will be 
reviewing the actual business rules for these sessions further down, along with the technical
decisions on how to tackle them.

But first, lets talk about the technological requirements and how to start the application in case you 
decide to clone this repository into your machine!

##Starting Polls API

The requirements to run the application are:

 - A Java development environment consisting of:
   - Java Development Kit 11+
   - Gradle
     - You can use an IDE such as IntelliJ, or the command line because Gradle is wrapped into 
       this repository
 - Docker Engine and Docker Compose
   - Both are included with Docker Desktop installation in Mac and Windows operating systems

The easiest way to start the Polls API is:

 - Clone the repository and go to the root folder 
 - Use Docker Compose to boot the external dependencies (MongoDB, RabbitMQ):
   > docker-compose up
 - Use Gradle to boot the application:
     - If you prefer (or need to debug the application), you can launch the app through an IDE.
   > gradlew bootRun
 - To interact with the API itself you can either use a tool such as Postman or Insomnia, or access
   SwaggerUI if you don't have such a tool installed!
   > localhost:8080

##The Rules of Polling

These are the business requirements for our Polling Session application:

 - The users must be able to create Polls
 - The users must be able to open a voting session for a Poll, which lasts for a requested time
   - Or one minute by default
 - The users should be able to cast YES/NO votes for open voting sessions
   - Only one vote per person per Poll
 - The results of the voting session must be calculated on session closure

There are some extra technical requirements too:
 
 - The data must persist through application restarts
 - The poll results must be published through a message broker
 - The application must be scalable

