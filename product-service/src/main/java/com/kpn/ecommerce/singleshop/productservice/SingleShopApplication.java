package com.kpn.ecommerce.singleshop.productservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SingleShopApplication {

  public static void main(String[] args) {
    log.info("Starting the SingleShopApplication...");
    SpringApplication.run(SingleShopApplication.class, args);
  }
}
