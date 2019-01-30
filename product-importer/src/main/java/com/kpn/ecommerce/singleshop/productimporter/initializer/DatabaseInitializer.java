package com.kpn.ecommerce.singleshop.productimporter.initializer;

import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * Contains methods to initialize a database by dropping all the indexes, constraints, and the data.
 */
@Slf4j
public final class DatabaseInitializer implements Initializer {

  private final QueryRunner queryRunner;

  public DatabaseInitializer(QueryRunner queryRunner) {
    this.queryRunner = queryRunner;
  }

  public void initialize() {
    log.info("Attempting to initialize the database...");
    log.info("Deleting all the data");
    queryRunner.execute(MobileDataQuery.DELETE_ALL_DATA_QUERY);
    log.info("[done]");
    log.info("Dropping all the indexes and constraints...");
    queryRunner.execute(MobileDataQuery.DROP_ALL_INDEXES_AND_CONSTRAINTS_QUERY);
    log.info("[done]");
  }
}
