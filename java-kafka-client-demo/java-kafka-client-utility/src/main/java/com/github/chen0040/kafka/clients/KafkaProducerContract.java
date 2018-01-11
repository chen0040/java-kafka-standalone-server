package com.github.chen0040.kafka.clients;


/**
 * Created by xschen on 11/29/16.
 */
public interface KafkaProducerContract {
   void setBrokers(String brokers);

   void write(String topic, String json);
}
