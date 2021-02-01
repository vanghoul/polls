# VeeGee's Polls API

Hello there!

This is a Spring Boot application I wrote in late January 2021 as part of a challenge!

The scenario for this API is helping a local community organize their Polling Sessions.  
We will be 
reviewing the actual business rules for these sessions further down, along with the technical
decisions on how to tackle them.

But first, lets talk about the technological requirements and how to start the application in case you 
decide to clone this repository into your machine!

## Starting Polls API

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

## The Rules of Polling

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

## The Design of the Solution

In order to achieve these requirements I decided to use the following techniques and tools:

 - Create a RESTful API to offer a simple and flexible interface to users
 - Use a MongoDB repository for the simplicity and scalability it offers
 - Use a RabbitMQ message broker, again, for the simplicity to setup and run
 - Offer all the external dependencies through Docker to avoid installations and setups that 
   could go awry with all the different machines and configurations

Can you notice a trend going on here?

As a developer, I really believe code and solutions should be kept as simple and clean as possible
for we and all the other people involved in our line of work :) !

Going back to the solution, I used the Spring Framework to implement all this.  
Besides my unbiased high opinion of it being a Java Developer, I believe the following sub-frameworks are easy to use and
get the job done pretty well:

 - Spring Data Rest to create the RESTful API
 - Spring Cloud Stream to interact with message brokers in a generic, highly maintainable way
 - Spring Scheduled Tasks to be able to check Polls closing and publishing the results
   - This is not as scalable as I would like, but we will talk about this soon!

### The Domain Model

Before discussing the solution in greater detail I would like to put forward the Poll model.

```
{
   "id": "6015f2b60a14b8165114340e",
   "title": "Are all ravens black?",
   "status": "OPEN",
   "start": "2021-01-25T12:00:00",
   "end": "2021-01-30T22:00:00",
   "voters": [
       {
          "cpf": "12334854777",
          "vote": true
       },
    ],
   "result": null
}
```
Its code is available [here][1].

It has some pretty standard fields such as a unique identifier and a title, but of note here 
are two design choices I want to talk about:

 1. **Status:** The Poll object has a life cycle in the application, denoted by this field.  
    It goes from *NEW* when it is created, to *OPEN* when able to receive votes and then *CLOSED*.  
    To help avoid errors with these life cycle changes, the Poll is immutable in the code, and I use a
    [factory][2] to handle them.
    
 2. **Voters:** The voters of a Poll are not themselves a Document in the database.  
    They are an inner domain concept of the Poll and are checked for uniqueness on vote through their CPF field, 
    which is a kind of Brazilian Security Social Number (thus unique by their very concept!).  
    Although this opens some design discussion, I feel the main concern of the API was to expose the Poll resource, 
    and thus I avoided recording Voters as their own Document in this first version of the application.

[1]: https://github.com/vanghoul/polls/blob/main/src/main/java/com/veegee/polls/business/model/Poll.java
[2]: https://github.com/vanghoul/polls/blob/main/src/main/java/com/veegee/polls/business/model/factory/PollFactory.java#L40
   
### The Restful API

The Poll resource can be interacted with through the following path: */api/v1/polls*.  
The code for its controller is available [here][3].

It supports the following HTTP verbs:

 - **GET:** retrieves all the existing Polls along with their votes.  
   There is no support for retrieval of single Polls, as I feel it doesn't add much to the application at this time.
   There is also no support for pagination yet, which is very clear improvement to the future in terms of performance.
 - **POST:** used to create new Polls, which are always created with a *NEW* status.
 - **PATCH:** used to change fields of existing Polls, though only changing the Status from *NEW* to *OPEN* is 
   supported as of now.  
   Honestly, I don't believe any other fields of a Poll should be able to be changed by the user, a part from the 
   End time, *maybe*. It would have to be discussed with great attention to security as to avoid, for example, 
   changing the titles of already concluded Polls for malicious purposes.

Aside from these, to cast votes the user must send a **POST** to */api/v1/polls/{id}/vote*.  
Votes can only be successfully inserted if:
 - The Poll is **OPEN**
 - The voter has not voted in this Poll before
 - The voter CPF is valid 
   - This validation is done through a fake CPF validator made available for this challenge
   - This validation result seems to be totally random, apart from checking if the CPF has 
     11 digits (which is the right CPF size, by the way)

This here is the single most discussable design choice in the application, in my opinion.  
While it looks rather strange, and it can be discussed how really RESTful this is, at the time
of implementation this was very simple and straight forward to implement, so I thought it was
a valid tradeoff.

It is also another very clear improvement for future versions of the API.

[3]: https://github.com/vanghoul/polls/blob/main/src/main/java/com/veegee/polls/api/controller/PollController.java


### The MongoDB Repository

For this application I decided to use MongoDB as the database.

The main reasons were:
 - It is very easy to setup, having an official, ready to use image from Docker Hub
 - It is also very scalable, being one of the few databases that already has a Reactive driver as of this time
   - Not that this application is ever going that way; I would write a new application using WebFlux if that 
     was the intention

Other than that (and to fill this section a little!), MongoDB has a very easy to use embedded
version* that is perfect for writing [narrow integration tests][4], [which I happen to be a huge fan of][5].

*It is not an official part of MongoDB, but it is a well known and used dependency that you can check out [here][6].

[4]: https://martinfowler.com/bliki/IntegrationTest.html
[5]: https://github.com/vanghoul/polls/blob/main/src/test/java/com/veegee/polls/PollsIT.java
[6]: https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo

