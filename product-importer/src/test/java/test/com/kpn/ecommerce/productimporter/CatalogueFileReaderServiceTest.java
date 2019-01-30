package test.com.kpn.ecommerce.productimporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Sets;
import com.kpn.ecommerce.singleshop.productimporter.Main;
import com.kpn.ecommerce.singleshop.productimporter.reader.CatalogueFileReaderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
public class CatalogueFileReaderServiceTest {

  @Autowired private CatalogueFileReaderService catalogueFileReaderService;

  @SuppressWarnings("unchecked")
  @Test
  public void testGetJsonAsMap() {
    Map<String, Object> jsonAsMap = catalogueFileReaderService.getJsonAsMap();
    assertFalse("Expected the json map to be populated.", jsonAsMap.isEmpty());
    assertEquals("Expected 7 entries in the jsonMap", 7, jsonAsMap.size());
    assertTrue(
        "Expected keys do not match",
        jsonAsMap
            .keySet()
            .equals(
                Sets.newHashSet(
                    "options",
                    "option_groups",
                    "subscriptions",
                    "promotions",
                    "hardwares",
                    "commercial_groups",
                    "countries")));
    assertEquals(
        "Expected no. of options do not match.",
        354,
        ((List<Map<String, Object>>) jsonAsMap.get("options")).size());
    
    assertEquals(
        "Expected no. of option_groups do not match.",
        37,
        ((List<Map<String, Object>>) jsonAsMap.get("option_groups")).size());
    
    assertEquals(
        "Expected no. of subscriptions do not match.",
        269,
        ((List<Map<String, Object>>) jsonAsMap.get("subscriptions")).size());
    
    assertEquals(
        "Expected no. of promotions do not match.",
        1210,
        ((List<Map<String, Object>>) jsonAsMap.get("promotions")).size());
    
    assertEquals(
        "Expected no. of hardwares do not match.",
        570,
        ((List<Map<String, Object>>) jsonAsMap.get("hardwares")).size());
    
    assertEquals(
        "Expected no. of commercial_groups do not match.",
        30,
        ((List<Map<String, Object>>) jsonAsMap.get("commercial_groups")).size());
    
    assertEquals(
        "Expected no. of countries do not match.",
        237,
        ((List<Map<String, Object>>) jsonAsMap.get("countries")).size());
  }
}
