package com.kpn.ecommerce.singleshop.productimporter.mobile.query.runners;

import java.util.Map;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.springframework.util.Assert;

import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;

public class MobileDataCatalogueQueryRunner implements QueryRunner {

  private final Driver driver;

  public MobileDataCatalogueQueryRunner(Driver driver) {
    Assert.notNull(driver, "Driver instance cannot be null.");
    this.driver = driver;
  }

  @Override
  public void execute(String query, Map<String, Object> params) {
    try (Session session = driver.session()) {
      session.writeTransaction(tx -> tx.run(new Statement(query, params == null ? null : params)));
    }
  }

  @Override
  public void execute(String query) {
    try (Session session = driver.session()) {
      session.writeTransaction(tx -> tx.run(new Statement(query)));
    }
  }
}
