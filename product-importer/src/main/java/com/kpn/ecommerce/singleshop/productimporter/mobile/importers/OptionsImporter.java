package com.kpn.ecommerce.singleshop.productimporter.mobile.importers;

import static com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery.QUERY_CREATOR;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.constants.NodeConstants;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that contains the concrete implementations of the {@link MobileDataImporter} for loading
 * Options from the mobile-catalogue.
 */
@Slf4j
public class OptionsImporter extends MobileDataImporter {

  public OptionsImporter(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    super(queryRunner, jsonAsMap);
  }

  @Override
  protected void runQuery() {
    log.info("Loading options");
    queryRunner.execute(MobileDataQuery.OPTIONS_QUERY, jsonAsMap);
    log.info("[done]");
  }

  @Override
  protected void createIndexes() {
    log.info("Creating index on option name");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_INDEX_ON_NAME, NodeConstants.OPTION));
    log.info("[done]");
  }

  @Override
  protected void createConstraints() {
    log.info("Creating unique constraint on option id");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.OPTION));
    log.info("[done]");
    log.info("Creating unique constraint on freeunit id");
    queryRunner.execute(
        QUERY_CREATOR.apply(
            MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.FREEUNIT));
    log.info("[done]");
    log.info("Creating unique constraint on extension id");
    queryRunner.execute(
        QUERY_CREATOR.apply(
            MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.EXTENSION));
    log.info("[done]");
  }
}
