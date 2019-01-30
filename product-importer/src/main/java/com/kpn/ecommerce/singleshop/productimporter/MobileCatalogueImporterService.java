package com.kpn.ecommerce.singleshop.productimporter;

import java.util.Map;

import com.kpn.ecommerce.singleshop.productimporter.initializer.DatabaseInitializer;
import com.kpn.ecommerce.singleshop.productimporter.initializer.Initializer;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.CommercialGroupsImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.CountriesImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.HardwareImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.MobileDataImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.OptionGroupsImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.OptionsImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.PromotionsImporter;
import com.kpn.ecommerce.singleshop.productimporter.mobile.importers.SubscriptionsImporter;

import lombok.extern.slf4j.Slf4j;

/** Class that is the starting point for the import process for mobile-catalogue. */
@Slf4j
public class MobileCatalogueImporterService implements CatalogueImporterService {

  private final QueryRunner queryRunner;
  private final Map<String, Object> jsonAsMap;

  public MobileCatalogueImporterService(QueryRunner queryRunner, Map<String, Object> jsonAsMap) {
    this.queryRunner = queryRunner;
    this.jsonAsMap = jsonAsMap;
  }

  /** Starting point of mobile-catalogue import process. */
  public void importCatalogue() {
    initDatabase(queryRunner);
    // TODO: should be a builder pattern that would create a hierarchy.
    log.info("Importing the mobile-catalogue...");
    MobileDataImporter optionsImporter = new OptionsImporter(queryRunner, jsonAsMap);
    optionsImporter.importData();
    MobileDataImporter optionGroupsImporter = new OptionGroupsImporter(queryRunner, jsonAsMap);
    optionGroupsImporter.importData();
    MobileDataImporter subscriptionsImporter = new SubscriptionsImporter(queryRunner, jsonAsMap);
    subscriptionsImporter.importData();
    MobileDataImporter hardwareImporter = new HardwareImporter(queryRunner, jsonAsMap);
    hardwareImporter.importData();
    MobileDataImporter promotionsImporter = new PromotionsImporter(queryRunner, jsonAsMap);
    promotionsImporter.importData();
    MobileDataImporter countriesImporter = new CountriesImporter(queryRunner, jsonAsMap);
    countriesImporter.importData();
    MobileDataImporter commercialGroupImporter =
        new CommercialGroupsImporter(queryRunner, jsonAsMap);
    commercialGroupImporter.importData();
  }

  private void initDatabase(QueryRunner queryRunner) {
    Initializer databaseInitializer = new DatabaseInitializer(queryRunner);
    databaseInitializer.initialize();
  }
}
