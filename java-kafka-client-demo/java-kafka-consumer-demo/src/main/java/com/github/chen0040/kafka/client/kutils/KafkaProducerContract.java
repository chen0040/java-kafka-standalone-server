package com.github.chen0040.kafka.client.kutils;


/**
 * Created by xschen on 11/29/16.
 */
public interface KafkaProducerContract {
   void write(String topic, String json);
}
