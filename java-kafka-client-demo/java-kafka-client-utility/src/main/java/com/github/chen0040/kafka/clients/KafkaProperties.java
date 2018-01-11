package com.github.chen0040.kafka.clients;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Created by low on 23/5/16.
 * Properties used by kafka
 */
@Getter
@Setter
public class KafkaProperties {
    private String kafkaBrokers = "localhost:9092";
    private String partitionerClass = "com.github.chen0040.kafka.client.KafkaPartitioner";
    private String keySerializer = StringSerializer.class.getName();
    private String valueSerializer = StringSerializer.class.getName();
    private String keyDeserializer = StringDeserializer.class.getName();
    private String valueDeserializer = StringDeserializer.class.getName();
    private String requestAck = "1";
}
