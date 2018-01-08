package com.github.chen0040.kafka.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Created by xschen on 15/9/2017.
 */
@SpringBootApplication
public class KafkaServerApplication {
   public static void main(String[] args) {
      SpringApplication.run(KafkaServerApplication.class, args);
   }
}
