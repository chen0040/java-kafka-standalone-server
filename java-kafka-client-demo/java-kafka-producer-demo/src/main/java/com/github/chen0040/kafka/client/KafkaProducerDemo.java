package com.github.chen0040.kafka.client;

import com.github.chen0040.kafka.clients.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class KafkaProducerDemo {
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
}
