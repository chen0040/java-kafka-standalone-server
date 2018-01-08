package com.github.chen0040.kafka.server.components;


import com.github.chen0040.kafka.server.services.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.allegro.tech.embeddedkafka.EmbeddedElastic;

import javax.annotation.PreDestroy;
import java.io.IOException;


/**
 * Created by xschen on 8/4/2017.
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

   @Autowired
   private VersionService versionService;

   @Autowired
   private EmbeddedElastic elasticSearch;

   private static final Logger logger = LoggerFactory.getLogger(Loader.class);

   @Override public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
      logger.info("profile: {}", versionService.getProfileString());

      if(versionService.isDefaultProfile()) {
         try {
            logger.info("starting elastic search server");
            elasticSearch.start();
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
         }
      }

   }

   @PreDestroy
   public void stopES(){
      if(versionService.isDefaultProfile()) {
         elasticSearch.stop();
      }
   }
}
