package com.github.chen0040.kafka.client.kutils;

import org.apache.kafka.clients.producer.Producer;
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
        Properties prop = new Properties();
        prop.put("bootstrap.servers", properties.getKafkaBrokers());
        prop.put("request.required.acks", properties.getRequestAck());
        prop.put("key.serializer", properties.getKeySerializer());
        prop.put("value.serializer", properties.getValueSerializer());
        prop.put("request.timeout.ms", 300000);

        //prop.put("partitioner.class", KafkaProperties.partitionerClass);
        producer = new org.apache.kafka.clients.producer.KafkaProducer(prop);
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
