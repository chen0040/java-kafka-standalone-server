package com.github.chen0040.kafka.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {

    @Value("${kafka.tcp_comm_port}")
    private int tcpPort;

    @Value("${kafka.cluster_name}")
    private String esClusterName;


}
