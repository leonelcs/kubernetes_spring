package com.kpn.ecommerce.singleshop.productimporter.mobile.importers;

import static com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery.QUERY_CREATOR;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.constants.NodeConstants;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that contains the concrete implementations of the {@link MobileDataImporter} for loading
 * Countries from the mobile-catalogue.
 */
@Slf4j
public class CountriesImporter extends MobileDataImporter {

  public CountriesImporter(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    super(queryRunner, jsonAsMap);
  }

  @Override
  protected void runQuery() {
    log.info("Loading countries");
    queryRunner.execute(MobileDataQuery.COUNTRY_QUERY, jsonAsMap);
    log.info("[done]");
  }

  @Override
  protected void createIndexes() {
    log.info("Creating index on country name");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_INDEX_ON_NAME, NodeConstants.COUNTRY));
    log.info("[done]");
  }

  @Override
  protected void createConstraints() {
    log.info("Creating unique constraint on country id");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.COUNTRY));
    log.info("[done]");
  }
}
