# Agile Engine Transaction Manager

A microservice that exposes endpoints that allow managing transactions.

## API Documentation
API documentation is available through Swagger in the URL http://[host]:port/swagger-ui.html.
A summary of the API documentation follows:


### Installing and Running
To build the project (running the tests) run
```
./gradlew clean build
```

To start the microservice run:

```
./gradlew bootRun
```

You can make requests from curl, Postman to the localhost and port specified (default is localhost:8080) but you can also access the API Documentation and make requests using the Swagger UI:
To access Swagger UI, in a browser enter the URL:

```
http://localhost:8080/swagger-ui.html
```

To stop the microservice press Ctr + C in the terminal.

It is not necessary to provide an external database service since the microservice runs with its own in-memory HSQLDB database.
This can easily be changed by modyfying the property "url" in the file application.properties  with another jdbc url:
Initial SQL scripts are included unde resources/data.sql and are run automatically when the app starts.

To start the frontend go to src/main/frontend run for the first time:
```
npm install

```
To start the app run:

```
npm start

```
It will start the app at localhost:4200.
NOTE: In order to be able to test, CORS was disabled. The annotation @CrossOrigin(origins = {"http://localhost:4200"}) should be removed from the controller if this is going to be used in prod.
To stop the service run Ctr + C.


## Tests

As part of the run, tests are run automatically.
To run the backend tests only, run
```
./gradlew test
```

The project includes unit tests that can be found under the package "test" and integration tests that can be found under the "test/integration".
The Integration test included runs a complete cycle, posting a transaction, getting it by id, getting all the transactions and getting the balance.

Current coverage is 100% for class, 91% for methods and 88% for lines of code.

To run the frontend tests, go to src/main/frontend and run
```
npm test
``` 


# Depedencies

Backend
* [Java 8 or higher](www.java.com)
* [Gradle](www.gradle.com) to build and launch the microservice.
* [Spring Boot](https://spring.io/projects/spring-boot) basic classes for microservices, including Spring Data that provide basic Repositories.
* [Swagger2](https://swagger.io/) API documentation generator.
* [HSQLDB](http://hsqldb.org/) In-memory database.
* JPA standard to manage the Content entity.
* [Log4j](https://logging.apache.org/log4j/2.x/) Log tool.
* [JUnit 5](https://junit.org/junit5/) For tests.
* [Mockito](https://site.mockito.org/) For mocking test components.

Frontend 
* [Angular 5](https://angular.io/)
* [Jasmine 2.8.0 or higher](https://jasmine.github.io/) for testing.
* [Karma 2.0.0](https://karma-runner.github.io/latest/index.html) for testing.
* [Typescript 2](https://www.typescriptlang.org/)
* [npm](https://www.npmjs.com/)

## Author

* María Florencia Pereira (mflorenciapereira@gmail.com)