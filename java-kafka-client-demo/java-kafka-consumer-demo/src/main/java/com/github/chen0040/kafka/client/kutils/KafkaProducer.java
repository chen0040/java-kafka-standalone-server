package com.github.chen0040.kafka.client.kutils;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Created by low on 14/5/16.
 * Producer instance
 */
public class KafkaProducer implements KafkaProducerContract, AutoCloseable {

    private Producer<String, String> producer;

    private static KafkaProducer instance = null;
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaProperties properties = new KafkaProperties();

    private KafkaProducer() {

    }

    public static synchronized KafkaProducerContract getInstance() {
        if(instance == null) {
            instance = new KafkaProducer();
        }
        return instance;
    }

    public void configure(KafkaProperties properties) {
        this.properties = properties;
    }

    private void open(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                properties.getKafkaBrokers());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 2);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        //prop.put("partitioner.class", KafkaProperties.partitionerClass);
        producer = new org.apache.kafka.clients.producer.KafkaProducer(props);
    }

    public synchronized void write(String topic, String json) {
        if(producer == null) {
            open();
        }

        logger.info("sending from producer...");
        producer.send(new ProducerRecord<>(topic, json), (metadata, e) -> {
            if(e != null) {
                logger.warn("exception while sending " + json);
                e.printStackTrace();
            }
            logger.info("Offset of " + json + ": " + metadata.offset());
        });

        /*producer.flush();
        if (future.isCancelled()) {
            logger.info("Send failed");
        }
        else if (future.isDone()) {*/
            logger.info("\"" + json + "\"" + " sent");
        //}
    }

    @Override
    public void close() throws Exception {
        if(producer != null){
            producer.close();
            producer = null;
        }
    }

    public boolean isOpened(){
        return producer != null;
    }
}
