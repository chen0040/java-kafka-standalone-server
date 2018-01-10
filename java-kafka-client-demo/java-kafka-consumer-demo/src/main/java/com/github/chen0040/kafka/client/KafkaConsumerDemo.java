package com.github.chen0040.kafka.client;

import com.github.chen0040.kafka.client.kutils.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class KafkaConsumerDemo {
    public static void main(String[] args) {
        String testString = "TEST " + new Date(System.currentTimeMillis());
        String topic = "kafkatest";
        List<String> topics = new ArrayList<>();
        KafkaConsumer consumer;
        KafkaProducerContract producer = KafkaProducer.getInstance();


        topics.add(topic);
        consumer = new KafkaConsumer(new KafkaProperties(), 1, "es-group", topics);

        ConsumerListener listener = (data) -> {
            System.out.println("consumer received: " + data);
            //receiptString[0] = data;
        };

        consumer.addListener(listener);
        Thread thread = new Thread(consumer);
        thread.start();

        producer.write(topic, testString);
        producer.write(topic, testString);
        producer.write(topic, testString);
        try {
            sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumer.shutdown();
    }
}
