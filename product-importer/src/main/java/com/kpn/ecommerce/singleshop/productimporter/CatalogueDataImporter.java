package com.kpn.ecommerce.singleshop.productimporter;

import java.util.Map;

import org.neo4j.driver.v1.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.kpn.ecommerce.singleshop.productimporter.config.ExternalPropertiesConfiguration;
import com.kpn.ecommerce.singleshop.productimporter.driver.DriverManager;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.reader.CatalogueFileReaderService;
import com.kpn.ecommerce.singleshop.productimporter.mobile.query.runners.MobileDataCatalogueQueryRunner;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("production")
public class CatalogueDataImporter {

  private final ExternalPropertiesConfiguration configuration;
  private final Map<String, Object> jsonAsMap;

  @Autowired
  public CatalogueDataImporter(
      ExternalPropertiesConfiguration configuration,
      CatalogueFileReaderService catalogueReaderService) {
    this.configuration = configuration;
    jsonAsMap = ImmutableMap.copyOf(catalogueReaderService.getJsonAsMap());
  }

  @EventListener(ApplicationReadyEvent.class)
  public void importCatalogue() {
    try (DriverManager driverManager =
        new DriverManager(
            configuration.getUri(), configuration.getUsername(), configuration.getPassword())) {
      Driver driver = driverManager.getDriver();
      QueryRunner queryRunner = new MobileDataCatalogueQueryRunner(driver);
      CatalogueImporterService mobileCatalogueImporterService =
          new MobileCatalogueImporterService(queryRunner, jsonAsMap);
      mobileCatalogueImporterService.importCatalogue();
    } catch (Exception e) {
      log.error("Error during import of mobile-catalogue.", e);
      System.exit(-1);
    }
  }
}
