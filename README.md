# java-kafka-standalone-server

A kafka standalone server for integration testing

# Feature

* Standalone kafka server that can run without additional installation on Windows, Linux and MacOS.
* Allow shut down of the kafka server remotely via web api call

# Usage
copy the kafka-standalone.jar to your directory and run the following command:

```bash
java -jar kafka-standalone.jar
```

This will start the kafka server at port 9200 (transport tcp port at 9350) and start another web server at port 9201.

To check whether the kafka server is alive, call the following url:

http://localhost:9201/ping

To kill the kafka server remotely, just call the following url:

http://localhost:9201/kill

# Note
In case you want to modify the behavior the kafka server and want to rebuild, you can run the make.ps1 on Windows
or make.sh on Linux or Mac.
