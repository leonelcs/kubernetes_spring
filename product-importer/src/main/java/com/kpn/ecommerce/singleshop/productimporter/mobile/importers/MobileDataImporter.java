package com.kpn.ecommerce.singleshop.productimporter.mobile.importers;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;

/** Template that defines the way to load data. */
public abstract class MobileDataImporter {

  protected final Map<String, Object> jsonAsMap;
  protected final QueryRunner queryRunner;

  protected MobileDataImporter(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    this.queryRunner = queryRunner;
    this.jsonAsMap = jsonAsMap;
  }

  /**
   * Template method that specifies the order in which data will be loaded by specifying the order
   * of the invocation.
   */
  public final void importData() {
    runQuery();
    createIndexes();
    createConstraints();
  }

  /** Method that is responsible for running the query for loading data. */
  protected abstract void runQuery();

  /** Method that is responsible for creating indexes on the loaded data. */
  protected abstract void createIndexes();

  /** Method that is responsible for creating constraints on the loaded data. */
  protected abstract void createConstraints();
}
