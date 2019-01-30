package com.kpn.ecommerce.singleshop.productimporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@ComponentScan(basePackages = {"com.kpn.ecommerce.singleshop.productimporter"})
@EnableTransactionManagement
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
