From openjdk:8u212-b04-jdk-stretch
MAINTAINER Arun "aruntcs2005@gmail.com"
EXPOSE 6015
WORKDIR /usr/local/bin
COPY ./target/springwithdynamodb-0.0.1-SNAPSHOT.war webapp.jar
CMD ["java", "-jar", "webapp.jar"]