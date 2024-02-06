package com.whatever.accumulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarketPriceAccumulatorApp {

  public static void main(String[] args) {
    SpringApplication.run(MarketPriceAccumulatorApp.class, args);
  }
}
