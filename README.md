[![CircleCI](https://circleci.com/gh/madmaux/diffs.svg?style=svg)](https://circleci.com/gh/madmaux/diffs)

# Diff service
The diff service is a HATEOS REST JSON based API that takes two base64 binaries and compare them to show the differences between them.

It consists of three endpoints two of them to indicates each part of the comparison (left operand and right operand) and one that compares the indicated parts and show the differences

Since this is a REST API each operand of the comparison is managed as a resource and stored in an in-memory database, this means that data will remind while the service is on

### How it works
To compare two base64 binaries we should provide each side of the comparison using a HTTP POST method hitting the next urls:

- POST [http://host:8000/${comparison_identifier}/left]()
- POST [http://host:8000/${comparison_identifier}/right]()

As we can see, each endpoint contains a `comparison identifier` which is a string representing the id we will use to identify the operation and a path indicating the side of the operand (left and right)
the request body is a JSON based content that contains the base64 binary that will be store in the database in the specified side of the operand and the response for these endpoints will be a JSON body containing the identifier, the base64 binary and a link to the next side or operand of the comparision 

once we have both sides of the comparison then we can hit the url to grab the differences:

- GET [http://host:8000/${comparison_identifier}]()

Again we have a `comparison identifier` that will help to take the two sides stored by the POST calls and the result will have the next data:
 
1. If the operands are equal or they are different in size then the service will return the right operand (this, that) 
2. If the operands has the same size then the service will return the offset and length off all the differences found in a map format where the key of the map is the offset of the difference found and the value is the length of that difference 

In both cases the service will also return the comparison identifier, and the links to the left and right operand

### Usage
Setting the left and right operands

Left operand
```shell
$ curl -X POST "http://localhost:8000/v1/diff/testId/left" -H "accept: application/hal+json" -H "Content-Type: application/json" -d "{ \"data\": \"YSB2AAA5IGxvbmcgc3RyaW5n\"}"
```
Response
```
{
  "data": "YSB2AAA5IGxvbmcgc3RyaW5n",
  "_links": {
    "next-side": {
      "href": "http://localhost:8000/v1/diff/testId/right"
    },
    "diff-results": {
      "href": "http://localhost:8000/v1/diff/testId"
    }
  }
}
```

Right opperand
```shell
$ curl -X POST "http://localhost:8000/v1/diff/testId/left" -H "accept: application/hal+json" -H "Content-Type: application/json" -d "{ \"data\": \"AAAYSB25IGxvbmcgcyaW5n3r\"}"
```
Response
```
{
  "data": "AAAYSB25IGxvbmcgcyaW5n3r",
  "_links": {
    "next-side": {
      "href": "http://localhost:8000/v1/diff/testId/left"
    },
    "diff-results": {
      "href": "http://localhost:8000/v1/diff/testId"
    }
  }
}
```
Comparison
```shell
$ curl -X GET "http://localhost:8000/v1/diff/testId" -H "accept: application/json"
```
Response
```
{
  "diffId": "testId",
  "diffsLocations": {
    "0": 7,
    "17": 7
  },
  "data": {
    "LEFT": "YSB2AAA5IGxvbmcgc3RyaW5n",
    "RIGHT": "AAAYSB25IGxvbmcgcyaW5n3r"
  },
  "_links": {
    "sides": [
      {
        "href": "http://localhost:8000/v1/diff/testId/left"
      },
      {
        "href": "http://localhost:8000/v1/diff/testId/right"
      }
    ],
    "self": {
      "href": "http://localhost:8000/v1/diff/testId"
    }
  }
}
```

You can find more information an test the service by running the app and check the documentation entering to the next link: [http://localhost:8000/]()

### Building
To build the project we use gradle and we put a gradle-wrapper so you don't need to install it

run the next line to build the project
```shell
$ ./gradlew clean build
```
### Run the project in a dev environment
```shell
$ ./gradlew boorRun
```
### Run in production
```shell
$ java -jar build/libs/waes-diff.jar
```
### If you prefer a docker container:
first generate the docker image and then run it
```shell
$ docker build -t waes-diff --build-arg jarFile=build/libs/waes-diff.jar .
$ docker run -d --rm --name waes-diff -p 8000:8000 waes-diff
```
### Tech
- [java v11]()
- [groovy lang v2.5.6](http://groovy-lang.org/)
- [gradle](https://gradle.org/)
- [spring-boot](https://spring.io/projects/spring-boot)
- [spring-hateos](https://spring.io/projects/spring-hateoas)
- [spring-data](https://spring.io/projects/spring-data)
- [spring-fox swagger2](https://springfox.github.io/springfox/)
- [lombock](https://projectlombok.org/)
- [orika](https://github.com/orika-mapper/orika)
- [spock](http://spockframework.org/)
- [docker](https://www.docker.com)
- [h2](http://www.h2database.com/html/main.html)

### Todos
- Write more tests
- Add a new endpoint that receive both operands at once

