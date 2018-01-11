# java-kafka-standalone-server

A kafka standalone server for integration testing

Supported version: kafka-0.10.0.2 (scala 2.11)

# Feature

* Standalone kafka server that can run without additional installation on Windows, Linux and MacOS.
* Allow shut down of the kafka server remotely via web api call

# Usage

The jars have been prebuilt and available in the root directory of the project, but the make.ps1 and make.sh can be 
used to rebuild the jars if you need to do some modification on your own. 

### Run Kafka standalone server

copy the kafka-standalone.jar to your directory and run the following command:

```bash
java -jar kafka-standalone.jar
```

This will start the kafka server at port 9092 (zk client port at 2181) and start another web server at port 9095.

To check whether the kafka server is alive, call the following url:

http://localhost:9095/ping

To kill the kafka server remotely, just call the following url:

http://localhost:9095/kill

### Test Kafka standalone server

The java-kafka-client-demo contains demo code on how to produce and consume topics in kafka. 

To test the kafka standalone server, with it still running, run the following two jars:

```bash
java -jar kafka-producer-demo.jar
java -jar kafka-consumer-demo.jar
```

The producer codes in kafka-producer-demo.jar are shown below:

```java
import com.github.chen0040.kafka.clients.*;
public static void main(String[] args) {
    String topic = "kafkatest";
    KafkaProducerContract producer = KafkaProducer.getInstance();
    producer.setBrokers("localhost:9092");
    
    while(true) {
        String testString = "TEST " + new Date(System.currentTimeMillis());
        System.out.println("produced: " + testString);
        producer.write(topic, testString);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

The consumer codes in java-kafka-consumer-demo.jar are shown below:

```java
import com.github.chen0040.kafka.clients.*;
public static void main(String[] args) {
    String topic = "kafkatest";
    List<String> topics = new ArrayList<>();
    topics.add(topic);

    String brokers = "localhost:9092";
    KafkaConsumer consumer = new KafkaConsumer(brokers, 1, "es-group", topics);


    ConsumerListener listener = (data) -> System.out.println("consumer received: " + data);

    consumer.addListener(listener);
    Thread thread = new Thread(consumer);
    thread.start();

    Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));

    while(true) {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
```


# Note
In case you want to modify the behavior the kafka server and want to rebuild, you can run the make.ps1 on Windows
or make.sh on Linux or Mac.
