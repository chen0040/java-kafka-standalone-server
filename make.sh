#!/usr/bin/env bash

mvn -f pom.xml clean package -Plocal -U

cp /target/java-kafka-standalone-server.jar kafka-standalone.jar

cd java-kafka-client-demo

mvn -f pom.xml clean package -Plocal -U

cp /target/java-kafka-consumer-demo.jar ../kafka-consumer-demo.jar

cp /target/java-kafka-producer-demo.jar ../kafka-producer-demo.jar

cd ..
