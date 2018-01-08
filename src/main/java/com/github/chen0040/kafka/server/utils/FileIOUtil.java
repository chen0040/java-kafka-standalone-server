package com.github.chen0040.kafka.server.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by xschen on 8/4/2017.
 */
public class FileIOUtil {

   public static InputStream getResource(String filename) throws IOException {
      ClassLoader classLoader = FileIOUtil.class.getClassLoader();
      URL dataFile = classLoader.getResource(filename);
      return dataFile.openStream();
   }
}
