package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DiscodeitApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
        args);
    String activeProfiles = String.join(", ", context.getEnvironment().getActiveProfiles());
    String port = context.getEnvironment().getProperty("server.port", "8080");

    System.out.printf("Active profile: %s%n",
        activeProfiles.isBlank() ? "default" : activeProfiles);
    System.out.printf("Application URL: http://localhost:%s/%n", port);
  }
}