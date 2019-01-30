package com.kpn.ecommerce.singleshop.productimporter.mobile.importers;

import static com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery.QUERY_CREATOR;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.constants.NodeConstants;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.MobileDataQuery;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that contains the concrete implementations of the {@link MobileDataImporter} for loading
 * Commercial-Groups from the mobile-catalogue.
 */
@Slf4j
public class CommercialGroupsImporter extends MobileDataImporter {

  public CommercialGroupsImporter(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    super(queryRunner, jsonAsMap);
  }

  @Override
  protected void runQuery() {
    log.info("Loading commercial-groups");
    queryRunner.execute(MobileDataQuery.COMMERCIAL_GROUP_QUERY, jsonAsMap);
    log.info("[done]");
  }

  @Override
  protected void createIndexes() {
    log.info("Creating index on commercial-group name");
    queryRunner.execute(
        QUERY_CREATOR.apply(MobileDataQuery.CREATE_INDEX_ON_NAME, NodeConstants.COMMERCIAL_GROUP));
    log.info("[done]");
  }

  @Override
  protected void createConstraints() {
    log.info("Creating unique constraint on commercial-group id");
    queryRunner.execute(
        QUERY_CREATOR.apply(
            MobileDataQuery.CREATE_UNIQUE_CONSTRAINT_ON_ID, NodeConstants.COMMERCIAL_GROUP));
    log.info("[done]");
  }
}
