#!/usr/bin/env bash

mvn -f pom.xml clean package -Plocal -U

cp /target/java-kafka-standalone-server.jar kafka-standalone.jar
