package test.com.kpn.ecommerce.productimporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.internal.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kpn.ecommerce.singleshop.productimporter.CatalogueImporterService;
import com.kpn.ecommerce.singleshop.productimporter.Main;
import com.kpn.ecommerce.singleshop.productimporter.MobileCatalogueImporterService;
import com.kpn.ecommerce.singleshop.productimporter.constants.AttributeConstants;
import com.kpn.ecommerce.singleshop.productimporter.constants.NodeConstants;
import com.kpn.ecommerce.singleshop.productimporter.constants.RelationShipConstants;
import com.kpn.ecommerce.singleshop.productimporter.query.QueryRunner;
import com.kpn.ecommerce.singleshop.productimporter.reader.CatalogueFileReaderService;

import apoc.coll.Coll;
import apoc.convert.Json;
import apoc.create.Create;
import apoc.index.FulltextIndex;
import apoc.load.LoadJson;
import apoc.load.Xml;
import apoc.meta.Meta;
import apoc.path.PathExplorer;
import apoc.refactor.GraphRefactoring;
import apoc.schema.Schemas;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
public class MobileCatalogueImportIntegrationTest {

  private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
  private static final String PATH_NAME =
      System.getProperty(JAVA_IO_TMPDIR)
          + File.separator
          + MobileCatalogueImportIntegrationTest.class.getName();
  private static final File GRAPH_DB_DIR = new File(PATH_NAME);

  private static final String ID_OPTION_WITH_EXTENSION = "378984315";
  private static final String ID_OPTION_WITHOUT_EXTENSION = "1104549825";
  private static final String ID_OPTION_GROUP = "1881638584";
  private static final String ID_COUNTRY = "NL";
  private static final String ID_SUBSCRIPTION_WITHOUT_EXTENSION = "503237751";
  private static final String ID_SUBSCRIPTION_WITH_EXTENSION = "581582890";

  private static final String ID_PROMOTION = "1483656995";
  private static final String ID_HARDWARE = "873906";

  private static final Integer ID_COMMERCIAL_GROUP = 61;

  private static final List<Class<?>> APOC_PROCEDURES =
      Lists.newArrayList(
          Coll.class,
          apoc.map.Maps.class,
          Json.class,
          Create.class,
          apoc.date.Date.class,
          FulltextIndex.class,
          apoc.lock.Lock.class,
          LoadJson.class,
          Xml.class,
          PathExplorer.class,
          Meta.class,
          GraphRefactoring.class,
          Schemas.class);

  private GraphDatabaseService graphDb;

  @Autowired private CatalogueFileReaderService service;

  // TODO: load data only once for all the tests.
  @Before
  public void prepareTestDatabase() {
    graphDb =
        new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(GRAPH_DB_DIR).newGraphDatabase();
    Procedures procedures =
        ((GraphDatabaseAPI) graphDb).getDependencyResolver().resolveDependency(Procedures.class);
    registerApocProcedures(procedures, APOC_PROCEDURES);
    QueryRunner queryRunner = new MobileDataCatalogueTestQueryRunner(graphDb);
    CatalogueImporterService importer =
        new MobileCatalogueImporterService(queryRunner, service.getJsonAsMap());
    importer.importCatalogue();
  }

  @After
  public void destroyTestDatabase() {
    graphDb.shutdown();
  }

  @AfterClass
  public static void deleteTestDatabaseFolder() throws IOException {
    FileUtils.forceDelete(GRAPH_DB_DIR);
  }

  @Test
  public void mobileCatalogueImport() {
    try (Transaction tx = graphDb.beginTx()) {
      testOptions();
      testOptionGroups();
      testSubscriptions();
      testPromotions();
      testHardwares();
      testCountries();
      testCommercialGroups();
      tx.success();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void testOptions() {

    assertEquals("Expected count of options do not match.", 354, getCount(NodeConstants.OPTION));

    Node optionWithoutExtension = getNode(NodeConstants.OPTION, ID_OPTION_WITHOUT_EXTENSION);

    // free-units of an option
    List<Node> freeUnits =
        getAssociatedNodes(
            optionWithoutExtension, RelationShipConstants.FREE_UNIT, Direction.INCOMING);
    assertFalse(
        "Expected multiple freeunits to be associated with Option ["
            + ID_OPTION_WITHOUT_EXTENSION
            + "]",
        freeUnits.isEmpty());
    assertEquals(
        "Expected 4 freeunits to be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        4,
        freeUnits.size());

    Set<Object> freeUnitIds = getIds(freeUnits);
    assertTrue(
        "Expect freeUnit ids do not match.",
        freeUnitIds.equals(Sets.newHashSet(1519539, 1519540, 1519541, 1519542)));

    // order-types of an option
    List<Node> orderTypes =
        getAssociatedNodes(
            optionWithoutExtension, RelationShipConstants.ORDER_TYPE, Direction.INCOMING);

    Set<Object> orderTypeIds = getIds(orderTypes);

    assertFalse(
        "Expected orderType to be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        orderTypeIds.isEmpty());
    assertEquals(
        "Expected 1 orderType to be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        1,
        orderTypeIds.size());
    assertTrue("Expect orderType ids do not match.", orderTypeIds.equals(Sets.newHashSet(19851)));

    // options of an option
    List<Node> subOptions =
        getAssociatedNodes(
            optionWithoutExtension, RelationShipConstants.CONTAINS, Direction.OUTGOING);

    Set<Object> subOptionIds = getIds(subOptions);

    assertFalse(
        "Expected subOption to be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        subOptionIds.isEmpty());
    assertEquals(
        "Expected 1 subOption to be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        1,
        subOptionIds.size());

    assertTrue(
        "Expect subOption ids do not match.",
        subOptionIds.equals(Sets.newHashSet(ID_OPTION_WITHOUT_EXTENSION)));

    // extension of an option
    List<Node> extensions =
        getAssociatedNodes(
            optionWithoutExtension, RelationShipConstants.EXTENSION, Direction.OUTGOING);
    assertTrue(
        "No extensions should be associated with Option [" + ID_OPTION_WITHOUT_EXTENSION + "]",
        extensions.isEmpty());

    // other fields of an option
    assertEquals(
        "Expected status to match.", "RELEASE", optionWithoutExtension.getProperty("status"));
    assertEquals(
        "Expected charge_type to match.", "MRC", optionWithoutExtension.getProperty("charge_type"));
    assertEquals(
        "Expected name to match.",
        "Zakelijk Mobiel Bellen EU",
        optionWithoutExtension.getProperty("name"));
    assertEquals(
        "Expected financial_code to match.",
        StringUtils.EMPTY,
        optionWithoutExtension.getProperty("financial_code"));
    assertEquals(
        "Expected mutation_date to match.",
        "2018-06-18T14:01:43Z",
        optionWithoutExtension.getProperty("mutation_date"));
    assertEquals(
        "Expected market_code to match.", "ALL", optionWithoutExtension.getProperty("market_code"));
    assertEquals(
        "Expected list_price_inc_vat to match.",
        "6.050000",
        optionWithoutExtension.getProperty("list_price_inc_vat"));
    assertEquals(
        "Expected list_price_exc_vat to match.",
        "5.000000",
        optionWithoutExtension.getProperty("list_price_exc_vat"));
    assertEquals(
        "Expected sale_start_date to match.",
        "2015-10-28T00:00:00Z",
        optionWithoutExtension.getProperty("sale_start_date"));
    assertEquals(
        "Expected sale_end_date to match.",
        "2017-05-31T18:00:00Z",
        optionWithoutExtension.getProperty("sale_end_date"));
    assertEquals(
        "Expected mb_transfer to match.",
        Boolean.FALSE.booleanValue(),
        optionWithoutExtension.getProperty("mb_transfer"));
    assertEquals(
        "Expected offer_type to match.",
        "ROAMING_DATA_BUNDLE",
        optionWithoutExtension.getProperty("offer_type"));
    assertEquals(
        "Expected insurance_category to match.",
        StringUtils.EMPTY,
        optionWithoutExtension.getProperty("insurance_category"));

    Node optionWithExtension = getNode(NodeConstants.OPTION, ID_OPTION_WITH_EXTENSION);

    // extension of an option when it exists.
    extensions =
        getAssociatedNodes(
            optionWithExtension, RelationShipConstants.EXTENSION, Direction.OUTGOING);
    assertFalse(
        "An extension should be associated with Option [" + ID_OPTION_WITH_EXTENSION + "]",
        extensions.isEmpty());
    assertEquals(
        "Only one extension should be associated with Option [" + ID_OPTION_WITH_EXTENSION + "]",
        1,
        extensions.size());
    Node extension = extensions.get(0);
    assertEquals("Expected ui_sort to match.", 0, extension.getProperty("ui_sort"));
    assertEquals(
        "Expected url to match.",
        "https://www.kpn.com/web/show/id=3622146",
        extension.getProperty("url"));
    assertEquals(
        "Expected tagline to match.",
        "Gedekt tegen kosten bij schade",
        extension.getProperty("tagline"));
    assertEquals(
        "Expected mutate_until_days to match.", 0, extension.getProperty("mutate_until_days"));
    assertEquals(
        "Expected mutate_until to match.",
        Boolean.FALSE.booleanValue(),
        extension.getProperty("mutate_until"));
    assertEquals(
        "Expected commercial_group_name to match.",
        "KPNToestelService",
        extension.getProperty("commercial_group_name"));
    assertEquals(
        "Expected mutate_never to match.",
        Boolean.FALSE.booleanValue(),
        extension.getProperty("mutate_never"));
    assertEquals(
        "Expected mutate_after_days to match.", 0, extension.getProperty("mutate_after_days"));
    assertEquals(
        "Expected mutate_after to match.",
        Boolean.FALSE.booleanValue(),
        extension.getProperty("mutate_after"));
    assertEquals(
        "Expected commercial_group to match.", 3, extension.getProperty("commercial_group"));
    assertEquals(
        "Expected addon to match.", Boolean.TRUE.booleanValue(), extension.getProperty("addon"));
    assertEquals(
        "Expected display to match.",
        Boolean.FALSE.booleanValue(),
        extension.getProperty("display"));
    assertEquals(
        "Expected commercial_group_label to match.",
        "Toestel Service",
        extension.getProperty("commercial_group_label"));
  }

  private void testOptionGroups() {

    assertEquals(
        "Expected count of optionGroups do not match.", 37, getCount(NodeConstants.OPTION_GROUP));

    Node optionGroup = getNode(NodeConstants.OPTION_GROUP, ID_OPTION_GROUP);

    // options of an option-group
    List<Node> options =
        getAssociatedNodes(optionGroup, RelationShipConstants.CONTAINS, Direction.OUTGOING);
    assertFalse(
        "Multiple options should be associated with OptionGroup [" + ID_OPTION_GROUP + "]",
        options.isEmpty());
    assertEquals(
        "Only three options should be associated with OptionGroup [" + ID_OPTION_GROUP + "]",
        3,
        options.size());
    Set<Object> collect = getIds(options);
    assertTrue(
        "Expected and retrieved options within OptionGroup [" + ID_OPTION_GROUP + "] do not match",
        collect.equals(Sets.newHashSet("1781635503", "1381634766", "1081635859")));

    assertEquals("Expected status to match.", "RELEASE", optionGroup.getProperty("status"));
    assertEquals(
        "Expected min_select_offer value to match.",
        1,
        optionGroup.getProperty("min_select_offer"));
    assertEquals("Expected name value to match.", "Zeker Mobiel", optionGroup.getProperty("name"));
    assertEquals(
        "Expected mutation_date value to match.",
        "2018-06-18T14:02:49Z",
        optionGroup.getProperty("mutation_date"));
    assertEquals(
        "Expected sale_start_date value to match.",
        "2010-01-01T00:00:00Z",
        optionGroup.getProperty("sale_start_date"));
    assertEquals(
        "Expected max_select_offer value to match.",
        1,
        optionGroup.getProperty("max_select_offer"));
    assertEquals(
        "Expected sale_end_date value to match.",
        "2015-01-01T00:00:00Z",
        optionGroup.getProperty("sale_end_date"));
  }

  private void testSubscriptions() {

    assertEquals(
        "Expected count of subscriptions do not match.", 269, getCount(NodeConstants.SUBSCRIPTION));

    Node subscription = getNode(NodeConstants.SUBSCRIPTION, ID_SUBSCRIPTION_WITHOUT_EXTENSION);

    // free-units of a subscription.
    List<Node> freeUnits =
        getAssociatedNodes(subscription, RelationShipConstants.FREE_UNIT, Direction.OUTGOING);
    assertFalse(
        "Expected multiple freeunits to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        freeUnits.isEmpty());
    assertEquals(
        "Expected 4 freeunits to be associated with Option ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        4,
        freeUnits.size());
    Set<Object> freeUnitIds = getIds(freeUnits);
    assertTrue(
        "The free-unit ids associated with the Subscription with id ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "] do not match",
        freeUnitIds.equals(Sets.newHashSet(1520771, 1520772, 1520773, 1520774)));

    // contract-durations of a subscription.
    List<Node> contractDurations =
        getAssociatedNodes(
            subscription, RelationShipConstants.CONTRACT_DURATION, Direction.OUTGOING);
    assertFalse(
        "Expected multiple contractDurations to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        contractDurations.isEmpty());
    assertEquals(
        "Expected 2 freeunits to be associated with Option ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        2,
        contractDurations.size());
    Set<Object> contractDurationIds = getIds(contractDurations);
    assertTrue(
        "The contractDuration ids associated with the Subscription with id ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "] do not match",
        contractDurationIds.equals(Sets.newHashSet(20201, 20202)));

    // order-types of a subscription.
    List<Node> orderTypes =
        getAssociatedNodes(subscription, RelationShipConstants.ORDER_TYPE, Direction.OUTGOING);
    assertFalse(
        "Expected 1 orderType to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        orderTypes.isEmpty());
    assertEquals(
        "Expected 1 orderType to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        1,
        orderTypes.size());
    Set<Object> orderTypeIds = getIds(orderTypes);
    assertTrue(
        "The orderType ids associated with the Subscription with id ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "] do not match",
        orderTypeIds.equals(Sets.newHashSet(19851)));

    // option-group of a subscription.
    List<Node> optionGroups =
        getAssociatedNodes(subscription, RelationShipConstants.OPTION_GROUP, Direction.OUTGOING);
    assertFalse(
        "Expected one optionGroup to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        optionGroups.isEmpty());
    assertEquals(
        "Expected 1 optionGroup to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        1,
        optionGroups.size());
    Set<Object> optionGroupIds = getIds(optionGroups);
    assertTrue(
        "The optionGroup ids associated with the Subscription with id ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "] do not match",
        optionGroupIds.equals(Sets.newHashSet("1604046368")));

    // options of a subscription.
    List<Node> options =
        getAssociatedNodes(subscription, RelationShipConstants.CONTAINS, Direction.OUTGOING);
    assertFalse(
        "Expected multiple options to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        options.isEmpty());
    assertEquals(
        "Expected 11 optionGroup to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "]",
        11,
        options.size());
    Set<Object> optionIds = getIds(options);
    assertTrue(
        "The option ids associated with the Subscription with id ["
            + ID_SUBSCRIPTION_WITHOUT_EXTENSION
            + "] do not match",
        optionIds.equals(
            Sets.newHashSet(
                "760175535",
                "104615603",
                "104117831",
                "1904123097",
                "960174742",
                "137793496",
                "1804116523",
                "1504117492",
                "1504616218",
                "1804119899",
                "381578792")));

    // other fields of a subscription.
    assertEquals("Expected status to match.", "RELEASE", subscription.getProperty("status"));
    assertEquals(
        "Expected list_price_exc_vat to match.",
        "18.880000",
        subscription.getProperty("list_price_exc_vat"));
    assertEquals(
        "Expected sale_start_date to match.",
        "2014-01-01T00:00:00Z",
        subscription.getProperty("sale_start_date"));
    assertEquals("Expected portfolio to match.", "BM", subscription.getProperty("portfolio"));
    assertEquals(
        "Expected subscription_type to match.",
        "DATA_ONLY",
        subscription.getProperty("subscription_type"));
    assertEquals(
        "Expected mutation_date to match.",
        "2018-10-01T11:12:59Z",
        subscription.getProperty("mutation_date"));
    assertEquals(
        "Expected sale_end_date to match.",
        "2014-08-01T00:00:00Z",
        subscription.getProperty("sale_end_date"));
    assertEquals(
        "Expected financial_code to match.", "5102", subscription.getProperty("financial_code"));
    assertEquals(
        "Expected mb_transfer to match.",
        Boolean.FALSE.booleanValue(),
        subscription.getProperty("mb_transfer"));
    assertEquals("Expected brand to match.", "KPN", subscription.getProperty("brand"));
    assertEquals(
        "Expected name to match.",
        "Mobiel Internet 500 MB 4G SIM Only",
        subscription.getProperty("name"));
    assertEquals(
        "Expected mobile_number_type to match.",
        "097",
        subscription.getProperty("mobile_number_type"));
    assertEquals(
        "Expected list_price_inc_vat to match.",
        "22.840000",
        subscription.getProperty("list_price_inc_vat"));
    assertEquals(
        "Expected hardware_subtype to match.",
        StringUtils.EMPTY,
        subscription.getProperty("hardware_subtype"));
    assertEquals(
        "Expected postpaid to match.",
        Boolean.TRUE.booleanValue(),
        subscription.getProperty("postpaid"));
    assertEquals(
        "Expected lineup to match.",
        "Mobiel Internet 2012 SIM Only",
        subscription.getProperty("lineup"));

    // extension of a subscription.
    Node subscriptionWithExtension =
        getNode(NodeConstants.SUBSCRIPTION, ID_SUBSCRIPTION_WITH_EXTENSION);
    List<Node> extensions =
        getAssociatedNodes(
            subscriptionWithExtension, RelationShipConstants.EXTENSION, Direction.OUTGOING);
    assertFalse(
        "Expected one extension to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITH_EXTENSION
            + "]",
        extensions.isEmpty());
    assertEquals(
        "Expected 1 extension to be associated with Subscription ["
            + ID_SUBSCRIPTION_WITH_EXTENSION
            + "]",
        1,
        extensions.size());
    Node extension = extensions.get(0);
    assertEquals(
        "Expected url to match.",
        "https://www.kpn.com/web/show/id=4075851",
        extension.getProperty("url"));
    assertEquals(
        "Expected tagline to match.",
        "Onbeperkt sms en 1000 MB in NL en EU",
        extension.getProperty("tagline"));
    assertEquals(
        "Expected roaming_region to match.", "UNKNOWN", extension.getProperty("roaming_region"));
    assertEquals(
        "Expected potential_mobile to match.",
        Boolean.FALSE.booleanValue(),
        extension.getProperty("potential_mobile"));
  }

  private void testPromotions() {

    assertEquals(
        "Expected count of promotions do not match.", 1210, getCount(NodeConstants.PROMOTION));

    Node promotion = getNode(NodeConstants.PROMOTION, ID_PROMOTION);

    // order-types of a Promotion.
    List<Node> orderTypes =
        getAssociatedNodes(promotion, RelationShipConstants.ORDER_TYPE, Direction.OUTGOING);
    assertFalse(
        "Expected 1 orderType to be associated with Promotion [" + ID_PROMOTION + "]",
        orderTypes.isEmpty());
    assertEquals(
        "Expected 1 orderType to be associated with Promotion [" + ID_PROMOTION + "]",
        1,
        orderTypes.size());
    Set<Object> orderTypeIds = getIds(orderTypes);
    assertTrue(
        "The orderType ids associated with the Promotion with id ["
            + ID_PROMOTION
            + "] do not match",
        orderTypeIds.equals(Sets.newHashSet(19851)));

    // subscriptions of a Promotion.
    List<Node> subscriptions =
        getAssociatedNodes(promotion, RelationShipConstants.SOLD_UNDER, Direction.INCOMING);
    assertFalse(
        "Expected 1 subscription to be associated with Promotion [" + ID_PROMOTION + "]",
        subscriptions.isEmpty());
    assertEquals(
        "Expected 1 subscription to be associated with Promotion [" + ID_PROMOTION + "]",
        1,
        subscriptions.size());
    Set<Object> subscriptionIds = getIds(subscriptions);
    assertTrue(
        "The subscription id associated with the Promotion with id ["
            + ID_PROMOTION
            + "] do not match",
        subscriptionIds.equals(Sets.newHashSet("1181582800")));

    // other fields of a promotion.
    assertEquals("Expected status to match.", "RELEASE", promotion.getProperty("status"));
    assertEquals(
        "Expected financial_code value to match.",
        StringUtils.EMPTY,
        promotion.getProperty("financial_code"));
    assertEquals(
        "Expected promotion_family to match.",
        "Kortingsfamilie 12",
        promotion.getProperty("promotion_family"));
    assertEquals(
        "Expected name to match.",
        "Korting zolang je abonnement loopt",
        promotion.getProperty("name"));
    assertEquals(
        "Expected mutation_date to match.",
        "2018-10-01T11:07:38Z",
        promotion.getProperty("mutation_date"));
    assertEquals("Expected value to match.", "15.000000", promotion.getProperty("value"));
    assertEquals("Expected mode to match.", "PERCENT", promotion.getProperty("mode"));
    assertEquals("Expected cross_sell to match.", "N", promotion.getProperty("cross_sell"));
    assertEquals(
        "Expected handset_bundle to match.",
        StringUtils.EMPTY,
        promotion.getProperty("handset_bundle"));
    assertEquals(
        "Expected sale_start_date to match.",
        "2001-01-01T00:00:00Z",
        promotion.getProperty("sale_start_date"));
    assertEquals(
        "Expected sale_end_date to match.",
        "2015-01-01T00:00:00Z",
        promotion.getProperty("sale_end_date"));
    assertEquals("Expected value_unit to match.", "PERCENT", promotion.getProperty("value_unit"));
  }

  private void testHardwares() {

    assertEquals(
        "Expected count of hardwares do not match.", 570, getCount(NodeConstants.HARDWARE));

    Node hardware = getNode(NodeConstants.HARDWARE, ID_HARDWARE);

    // prices of a hardware.
    List<Node> prices =
        getAssociatedNodes(hardware, RelationShipConstants.PRICED_AS, Direction.OUTGOING);
    assertFalse(
        "Expected multiple prices to be associated with Hardware [" + ID_HARDWARE + "]",
        prices.isEmpty());
    assertEquals(
        "Expected 2 prices to be associated with Hardware [" + ID_HARDWARE + "]", 2, prices.size());
    Set<Object> priceIds = getIds(prices);
    assertTrue(
        "The price ids associated with the Hardware with id [" + ID_HARDWARE + "] do not match",
        priceIds.equals(Sets.newHashSet(822643, 822718)));

    // other fields of a hardware.
    assertEquals(
        "Expected hardware_type to match.", "HANDSET", hardware.getProperty("hardware_type"));
    assertEquals("Expected network_type to match.", "4G", hardware.getProperty("network_type"));
    assertEquals(
        "Expected sticky to match.", Boolean.FALSE.booleanValue(), hardware.getProperty("sticky"));
    assertEquals(
        "Expected description to match.", "Nokia 6, Copper", hardware.getProperty("description"));
    assertEquals(
        "Expected commercial_product_name to match.",
        "Nokia 6, Copper",
        hardware.getProperty("commercial_product_name"));
    assertEquals(
        "Expected sale_end_date to match.",
        "2018-07-06T00:00:00Z",
        hardware.getProperty("sale_end_date"));
    assertEquals(
        "Expected guarantee_period to match.", 24, hardware.getProperty("guarantee_period"));
    assertEquals("Expected ean_code to match.", "6438409005885", hardware.getProperty("ean_code"));
    assertEquals(
        "Expected simcard_type to match.", StringUtils.EMPTY, hardware.getProperty("simcard_type"));
    assertEquals(
        "Expected sale_start_date to match.",
        "2017-08-07T00:00:00Z",
        hardware.getProperty("sale_start_date"));
    assertEquals("Expected vat_code to match.", "21", hardware.getProperty("vat_code"));
    assertEquals(
        "Expected deviating_range to match.",
        StringUtils.EMPTY,
        hardware.getProperty("deviating_range"));
    assertEquals(
        "Expected hardware_sub_type to match.",
        "HandsetToestel",
        hardware.getProperty("hardware_sub_type"));
    assertEquals("Expected os to match.", "ANDROID", hardware.getProperty("os"));
    assertEquals(
        "Expected insurance_category to match.",
        StringUtils.EMPTY,
        hardware.getProperty("insurance_category"));
  }

  private void testCountries() {

    assertEquals("Expected count of countries do not match.", 237, getCount(NodeConstants.COUNTRY));

    Node country = getNode(NodeConstants.COUNTRY, ID_COUNTRY);

    // other fields of a  country.
    assertEquals(
        "Expected roaming_region values to match.",
        "EUROPE",
        country.getProperty("roaming_region"));
    assertEquals(
        "Expected roaming_bundle_category values to match.",
        24,
        country.getProperty("roaming_bundle_category"));
    assertEquals("Expected names to match.", "Nederland", country.getProperty("name"));
  }

  private void testCommercialGroups() {

    assertEquals(
        "Expected count of commercialGroups do not match.",
        30,
        getCount(NodeConstants.COMMERCIAL_GROUP));

    Node commercialGroup = getNode(NodeConstants.COMMERCIAL_GROUP, ID_COMMERCIAL_GROUP);

    // other fields of a commercial-group.
    assertEquals(
        "Expected id values to match.", ID_COMMERCIAL_GROUP, commercialGroup.getProperty("id"));
    assertEquals(
        "Expected parent_group values to match.", 41, commercialGroup.getProperty("parent_group"));
    assertEquals(
        "Expected label values to match.",
        "Buiten EU,  voordeellanden",
        commercialGroup.getProperty("label"));
    assertEquals(
        "Expected names to match.", "KPNWorldVoordeelBundel", commercialGroup.getProperty("name"));
  }

  private Node getNode(String label, Object id) {
    Assert.notNull(label, "Expected a valid label.");
    Assert.notNull(id, "Expected a valid id value.");
    Node node = graphDb.findNode(Label.label(label), AttributeConstants.ID, id);
    assertEquals("Expected id field values to match.", id, node.getProperty("id"));
    return node;
  }

  private long getCount(String label) {
    Assert.notNull(label, "Expected a valid label.");
    return graphDb.findNodes(Label.label(label)).stream().count();
  }

  private Set<Object> getIds(List<Node> nodes) {
    return nodes
        .stream()
        .map(option -> option.getProperty(AttributeConstants.ID))
        .collect(Collectors.toSet());
  }

  private List<Node> getAssociatedNodes(Node node, String relationShip, Direction direction) {
    Assert.notNull(direction, "Relationship Direction must be specified.");
    Iterable<Relationship> relationShips =
        node.getRelationships(RelationshipType.withName(relationShip), direction);
    Stream<Relationship> stream = StreamSupport.stream(relationShips.spliterator(), false);
    return stream
        .map(
            relation ->
                direction.equals(Direction.INCOMING)
                    ? relation.getStartNode()
                    : relation.getEndNode())
        .collect(Collectors.toList());
  }

  private void registerApocProcedures(Procedures procedures, List<Class<?>> apocProcedures) {
    Assert.notNull(procedures, "Valid procedures must be specified.");
    Assert.notNull(apocProcedures, "Valid list of apoc-procedures expected.");
    apocProcedures.forEach(
        (proc) -> {
          try {
            procedures.registerProcedure(proc);
          } catch (KernelException e) {
            throw new RuntimeException(
                "Error registering apoc procedure :[" + proc.getName() + "]", e);
          }
        });
  }

  private static class MobileDataCatalogueTestQueryRunner implements QueryRunner {

    private final GraphDatabaseService graphDb;

    public MobileDataCatalogueTestQueryRunner(GraphDatabaseService graphDb) {
      this.graphDb = graphDb;
    }

    @Override
    public void execute(String query) {
      graphDb.execute(query);
    }

    @Override
    public void execute(String query, Map<String, Object> params) {
      graphDb.execute(query, params);
    }
  }
}
