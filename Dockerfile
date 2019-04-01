## Run the next line to generate the docker image:
# $ docker build -t waes-diff --build-arg jarFile=build/libs/waes-diff.jar .

## To run the container execute the next line:
# $ docker run -d --rm --name waes-diff -p 8000:8000 waes-diff

FROM openjdk:11-jdk-slim

VOLUME /opt

ARG jarFile

COPY ${jarFile} waes-diff/bin/waes-diff.jar

EXPOSE 8000

ENTRYPOINT ["java","-jar","waes-diff/bin/waes-diff.jar"]