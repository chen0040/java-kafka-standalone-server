package com.github.chen0040.kafka.client;

import com.github.chen0040.kafka.clients.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class KafkaConsumerDemo {
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
}
