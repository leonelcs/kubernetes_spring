package com.kpn.ecommerce.singleshop.productimporter.mobile.importers;

import static com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery.QUERY_CREATOR;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.constants.NodeConstants;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that contains the concrete implementations of the {@link MobileDataImporter} for loading
 * Subscriptions from the mobile-catalogue.
 */
@Slf4j
public class SubscriptionsImporter extends MobileDataImporter {

  public SubscriptionsImporter(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    super(queryRunner, jsonAsMap);
  }

  @Override
  protected void runQuery() {
    log.info("Loading subscriptions");
    queryRunner.execute(MobileDataQuery.SUBSCRIPTIONS_QUERY, jsonAsMap);
    log.info("[done]");
  }

  @Override
  protected void createIndexes() {
    log.info("Creating index on subscription name");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_INDEX_ON_NAME, NodeConstants.SUBSCRIPTION));
    log.info("[done]");
    log.info("Creating index on subscription saleEndDate");
    queryRunner.execute(
        QUERY_CREATOR.apply(
            MobileDataQuery.CREATE_INDEX_ON_SALE_END_DATE, NodeConstants.SUBSCRIPTION));
    log.info("[done]");
    log.info("Creating index on subscription lineup");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_INDEX_ON_LINEUP, NodeConstants.SUBSCRIPTION));
    log.info("[done]");
  }

  @Override
  protected void createConstraints() {
    log.info("Creating unique constraint on subscription id");
    queryRunner.execute(
        QUERY_CREATOR.apply(
            MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.SUBSCRIPTION));
    log.info("[done]");
  }
}
