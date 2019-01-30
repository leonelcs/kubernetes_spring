package com.kpn.ecommerce.singleshop.productimporter.config;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

// TODO: make static classes for modelling the properties
@Validated
@Configuration
@EnableConfigurationProperties
@Data
@Profile("production")
public class ExternalPropertiesConfiguration {

  @NotBlank
  @Value("${spring.data.neo4j.username}")
  private String username;

  @NotBlank
  @Value("${spring.data.neo4j.password}")
  private String password;

  @NotBlank
  @Value("${spring.data.neo4j.uri}")
  private String uri;
}
