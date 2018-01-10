package com.github.chen0040.kafka.server.configs;

import info.batey.kafka.unit.KafkaUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Value("${kafka.zkPort}")
    private int zkPort;

    @Value("${kafka.brokerPort}")
    private int brokerPort;

    @Bean
    public KafkaUnit kafka() {
        return new KafkaUnit(zkPort, brokerPort);
    }


}
