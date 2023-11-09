package com.danolog.api;

import com.danolog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class DanologApplication {

  public static void main(String[] args) {
    SpringApplication.run(DanologApplication.class, args);
  }

}
