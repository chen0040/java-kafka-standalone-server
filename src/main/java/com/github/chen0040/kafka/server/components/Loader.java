package com.github.chen0040.kafka.server.components;


import com.github.chen0040.kafka.server.services.VersionService;
import info.batey.kafka.unit.KafkaUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;


/**
 * Created by xschen on 8/4/2017.
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

   @Autowired
   private VersionService versionService;

   @Autowired
   private KafkaUnit kafkaUnitServer;

   private static final Logger logger = LoggerFactory.getLogger(Loader.class);

   @Override public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
      logger.info("profile: {}", versionService.getProfileString());

      if(versionService.isDefaultProfile()) {
         try {
            logger.info("starting kafka server");
            kafkaUnitServer.startup();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }

   @PreDestroy
   public void stopES(){
      if(versionService.isDefaultProfile()) {
         kafkaUnitServer.shutdown();
      }
   }
}
