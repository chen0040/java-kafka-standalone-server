package com.github.chen0040.kafka.client.kutils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by low on 13/5/16.
 * Consumer instance
 */
public class KafkaConsumer implements Runnable {
    private org.apache.kafka.clients.consumer.KafkaConsumer consumer;
    private List<String> topics;
    private int id;
    private List<TopicPartition> partition = new ArrayList<>();
    private List<ConsumerListener> listeners = new ArrayList<>();
    private KafkaProperties properties = new KafkaProperties();

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private boolean isPartitioned = false;

    public void addListener(ConsumerListener newListener){
        listeners.add(newListener);
    }

    public KafkaConsumer(KafkaProperties properties, int id, String groupId, List<String> topics, int partitionNumber) {
        this.properties = properties;
        setup(id, groupId, topics);
        for (String topic: topics) {
            this.partition.add(new TopicPartition(topic, partitionNumber));
        }
        isPartitioned = true;
    }

    public KafkaConsumer(KafkaProperties properties, int id, String groupId, List<String> topics) {
        this.properties = properties;
        setup(id, groupId, topics);
    }

    private void setup(int id, String groupId, List<String> topics) {
      this.id = id;
      this.topics = topics;
      Properties props = new Properties();
      props.put("bootstrap.servers", properties.getKafkaBrokers());
      props.put("group.id", groupId);
      props.put("key.deserializer", properties.getKeyDeserializer());
      props.put("value.deserializer", properties.getValueDeserializer());
      props.put("request.timeout.ms", 300000);
        props.put("session.timeout.ms", 180000);
      this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(props);
    }


    @Override
    public void run() {

        try {
            if (isPartitioned) {
                consumer.assign(partition);
            }
            else {
                consumer.subscribe(topics);
            }
            logger.info("Consumer subscribed");

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(300L);
                for (ConsumerRecord<String, String> record : records) {

                    for (ConsumerListener cl: listeners) {
                        cl.onReceipt(record.value());
                    }
                }
            }
        }
        catch (WakeupException e) {
            logger.info("shutting down");
        }
        finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}

