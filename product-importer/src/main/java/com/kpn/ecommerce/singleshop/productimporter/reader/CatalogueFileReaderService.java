package com.kpn.ecommerce.singleshop.productimporter.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Profile({"test", "production"})
public class CatalogueFileReaderService {

  private static final String MOBILE_CATALOGUE_JSON = "mobile_catalogue.json";

  private static final Supplier<String> MSG_ERROR =
      () -> "Error while reading file: [" + MOBILE_CATALOGUE_JSON + "]";

  private final ObjectMapper objectMapper;

  @Autowired
  public CatalogueFileReaderService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Map<String, Object> getJsonAsMap() {
    try (InputStream mobileCatalogueInputStream =
        CatalogueFileReaderService.class
            .getClassLoader()
            .getResourceAsStream(MOBILE_CATALOGUE_JSON)) {
      Assert.notNull(mobileCatalogueInputStream, MSG_ERROR);
      return objectMapper.readValue(
          mobileCatalogueInputStream, new TypeReference<Map<String, Object>>() {});
    } catch (IOException e) {
      throw new RuntimeException(MSG_ERROR.get(), e);
    }
  }
}
