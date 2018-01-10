package com.github.chen0040.kafka.client.kutils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
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
      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getKafkaBrokers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

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
            e.printStackTrace();
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

