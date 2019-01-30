package com.kpn.ecommerce.singleshop.productimporter.driver;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriverManager implements AutoCloseable {

  private final Driver driver;

  public DriverManager(String uri, String userName, String password) {
    Assert.isTrue(!StringUtils.isAllBlank(uri), () -> "Specify a valid neo4j remote uri.");
    Assert.isTrue(
        !StringUtils.isAllBlank(userName),
        () -> "Specify a valid username for connecting to a remote neo4j database.");
    Assert.isTrue(
        !StringUtils.isAllBlank(password),
        () -> "Specify a valid password for connecting to a remote neo4j database.");
    driver = GraphDatabase.driver(uri, AuthTokens.basic(userName, password));
    log.info("Succesfully instantiated the driver.");
  }

  public final Driver getDriver() {
    return this.driver;
  }

  @Override
  public void close() throws Exception {
    log.info("Attempting to close the driver instance {}.", this.driver);
    if (driver != null) {
      driver.close();
    }
    log.info("Driver instance {} closed successfully.", this.driver);
  }
}
