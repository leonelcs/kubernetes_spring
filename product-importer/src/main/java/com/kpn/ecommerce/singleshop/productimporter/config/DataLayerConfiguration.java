package com.kpn.ecommerce.singleshop.productimporter.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@ComponentScan(basePackages = "com.kpn.ecommerce.singleshop.productimporter")
@Configuration
@Profile("production")
public class DataLayerConfiguration {

  @Autowired private ExternalPropertiesConfiguration properties;

  @Bean
  public org.neo4j.ogm.config.Configuration configuration() {
    return new org.neo4j.ogm.config.Configuration.Builder()
        .uri(properties.getUri())
        .credentials(properties.getUsername(), properties.getPassword())
        .build();
  }

  @Bean
  public SessionFactory sessionFactory() {
    return new SessionFactory(configuration(), "com.kpn.ecommerce.singleshop.productimporter");
  }

  @Bean
  public Neo4jTransactionManager transactionManager() {
    return new Neo4jTransactionManager(sessionFactory());
  }
}
