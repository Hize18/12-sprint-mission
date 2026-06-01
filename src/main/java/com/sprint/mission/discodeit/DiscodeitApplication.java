package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {
//    의존성을 static으로 빼서 주입이 아니라 main에서 정의 및 주입으로
//    대신 파라미터로 매번 넘겨야 함.

  public static void main(String[] args) {
    SpringApplication.run(DiscodeitApplication.class, args);
    System.out.println("http://localhost:8080/");
  }
}