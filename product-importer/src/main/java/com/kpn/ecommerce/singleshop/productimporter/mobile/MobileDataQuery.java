package com.kpn.ecommerce.singleshop.productimporter.mobile;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.neo4j.driver.v1.Statement;

import com.kpn.ecommerce.singleshop.productimporter.constants.AttributeConstants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MobileDataQuery {

  private static final String CREATE_INDEX_QUERY = "CREATE INDEX ON :%s(%s)";

  public static final String COMPOSITE_INDEX_OPTION =
      "CREATE INDEX ON :Option(id, name, sale_end_date)";

  public static final String DELETE_ALL_DATA_QUERY = "MATCH(n) detach delete n";

  public static final String DROP_ALL_INDEXES_AND_CONSTRAINTS_QUERY =
      "CALL apoc.schema.assert({},{},true) YIELD label, key RETURN *";

  public static final Function<String, String> CREATE_INDEX_ON_ID =
      type -> String.format(CREATE_INDEX_QUERY, type, AttributeConstants.ID);

  public static final Function<String, String> CREATE_INDEX_ON_NAME =
      type -> String.format(CREATE_INDEX_QUERY, type, AttributeConstants.NAME);

  public static final Function<String, Statement> STATEMENT_SUPPLIER =
      query -> new Statement(query);

  // TODO: to make a common index creator based on attribute.
  public static final Function<String, String> CREATE_INDEX_ON_SALE_END_DATE =
      type -> String.format(CREATE_INDEX_QUERY, type, AttributeConstants.SALE_END_DATE);

  public static final Function<String, String> CREATE_INDEX_ON_LINEUP =
      type -> String.format(CREATE_INDEX_QUERY, type, AttributeConstants.LINEUP);

  public static final BiFunction<String, String, String> CREATE_INDEX_ON_ATTRIBUTE =
      (type, attribute) -> String.format(CREATE_INDEX_QUERY, type, attribute);

  public static final Function<String, String> CREATE_UNIQUE_CONSTRAINT_ON_ID =
      type ->
          String.format(
              "CREATE CONSTRAINT ON (n:%s) ASSERT n.%s IS UNIQUE", type, AttributeConstants.ID);

  public static final BiFunction<Function<String, String>, String, String> QUERY_CREATOR =
      (f, arg) -> f.apply(arg);

  // TODO: take the relations from the relationship constants.
  public static final String OPTIONS_QUERY =
      "UNWIND {options} as o "
          + "MERGE (option:Option {id:o.id}) ON CREATE SET "
          + "option.status = o.status, "
          + "option.charge_type = o.charge_type, "
          + "option.name = o.name, "
          + "option.financial_code = o.financial_code, "
          + "option.mutation_date = o.mutation_date, "
          + "option.market_code = o.market_code, "
          + "option.list_price_inc_vat = o.list_price_inc_vat, "
          + "option.contract_duration_condition = o.contract_duration_condition, "
          + "option.list_price_exc_vat = o.list_price_exc_vat, "
          + "option.sale_start_date = o.sale_start_date, "
          + "option.sale_end_date = o.sale_end_date, "
          + "option.mb_transfer = o.mb_transfer, "
          + "option.offer_type = o.offer_type, "
          + "option.insurance_category = o.insurance_category	"

          // create extension only when it is present
          + "FOREACH(_ IN CASE WHEN o.extension IS NOT NULL THEN [1] ELSE [] END | "
          + "MERGE (extension:Extension {id:o.id}) SET "
          + "extension.ui_sort = o.extension.ui_sort, "
          + "extension.url = o.extension.url, "
          + "extension.tagline = o.extension.tagline, "
          + "extension.tagline = o.extension.tagline, "
          + "extension.mutate_until_days = o.extension.mutate_until_days, "
          + "extension.mutate_until = o.extension.mutate_until, "
          + "extension.mutate_until = o.extension.mutate_until, "
          + "extension.commercial_group_name = o.extension.commercial_group_name, "
          + "extension.mutate_never = o.extension.mutate_never, "
          + "extension.mutate_after_days = o.extension.mutate_after_days, "
          + "extension.mutate_after = o.extension.mutate_after, "
          + "extension.commercial_group = o.extension.commercial_group, "
          + "extension.addon = o.extension.addon, "
          + "extension.display = o.extension.display, "
          + "extension.commercial_group_label = o.extension.commercial_group_label "
          + "MERGE (option)-[:EXTENSION]->(extension)) "
          + "FOREACH (ot IN o.order_types | "
          + "MERGE (ordertype:OrderType {id:ot.id}) ON CREATE SET "
          + "ordertype.name = ot.name "
          + "MERGE (option)<-[:ORDER_TYPE]-(ordertype)) "
          + "FOREACH (fu IN o.freeunits | "
          + "MERGE (freeunit:FreeUnit {id:fu.id}) ON CREATE SET "
          + "freeunit.amount = fu.amount, "
          + "freeunit.type = fu.type, "
          + "freeunit.unit = fu.unit "
          + "MERGE (option)<-[:FREE_UNIT]-(freeunit)) "

          // TODO: clarify with someone if multiple sub-options of the same type need to
          // be created.
          // eg: id: 1304616553
          + "FOREACH (so IN o.options | "
          + "CREATE (suboption:SubOption {id:so.id}) SET "
          + "suboption.type = so.type, "
          + "suboption.end_date = so.end_date, "
          + "suboption.start_date = so.start_date "
          + "MERGE (option)-[:CONTAINS]->(suboption)) ";

  public static final String OPTION_GROUPS_QUERY =
      "UNWIND {option_groups} as o "
          + "MERGE (option_group:OptionGroup{id:o.id}) ON CREATE SET "
          + "option_group.status = o.status, "
          + "option_group.min_select_offer = o.min_select_offer, "
          + "option_group.name = o.name, "
          + "option_group.mutation_date = o.mutation_date, "
          + "option_group.sale_start_date = o.sale_start_date, "
          + "option_group.sale_end_date = o.sale_end_date, "
          + "option_group.max_select_offer = o.max_select_offer "
          + "WITH o, option_group "
          + "UNWIND o.options as option_id "
          + "MATCH (option:Option {id:option_id}) "
          + "MERGE (option_group)-[:CONTAINS]->(option) ";

  public static final String SUBSCRIPTIONS_QUERY =
      "UNWIND {subscriptions} as s "
          + "MERGE (subscription:Subscription{id: s.id}) ON CREATE SET "
          + "subscription.list_price_exc_vat = s.list_price_exc_vat, "
          + "subscription.sale_start_date = s.sale_start_date, "
          + "subscription.sale_end_date = s.sale_end_date, "
          + "subscription.portfolio = s.portfolio, "
          + "subscription.subscription_type = s.subscription_type, "
          + "subscription.mutation_date = s.mutation_date, "
          + "subscription.status = s.status, "
          + "subscription.financial_code = s.financial_code, "
          + "subscription.brand = s.brand, "
          + "subscription.mb_transfer = s.mb_transfer, "
          + "subscription.name = s.name, "
          + "subscription.mobile_number_type = s.mobile_number_type, "
          + "subscription.list_price_inc_vat = s.list_price_inc_vat, "
          + "subscription.hardware_subtype = s.hardware_subtype, "
          + "subscription.postpaid = s.postpaid, "
          + "subscription.lineup = s.lineup "
          + "FOREACH (c IN s.contract_durations | "
          + "MERGE (contract_duration:ContractDuration {id:c.id}) ON CREATE SET "
          + "contract_duration.name = c.name "
          + "MERGE (subscription)-[:CONTRACT_DURATION]->(contract_duration)) "
          + "FOREACH (m IN s.mrcs | "
          + "MERGE (mrcs:Mrcs {id:m.id}) ON CREATE SET "
          + "mrcs.max_value = m.max_value, "
          + "mrcs.min_value = m.min_value, "
          + "mrcs.contract_duration_condition = m.contract_duration_condition, "
          + "mrcs.deviation_acquisition = m.deviation_acquisition, "
          + "mrcs.acquisition = m.acquisition, "
          + "mrcs.retention = m.retention, "
          + "mrcs.channel = m.channel "
          + "MERGE (subscription)-[:MRCS]->(mrcs)) "
          + "WITH s, subscription "
          + "UNWIND s.option_groups as option_group_id "
          + "MATCH (option_group:OptionGroup {id:option_group_id}) "
          + "MERGE (subscription)-[:OPTION_GROUP]->(option_group) "
          + "FOREACH (fu IN s.freeunits | "
          + "MERGE (freeunit:FreeUnit {id:fu.id}) ON CREATE SET "
          + "freeunit.amount = fu.amount, "
          + "freeunit.type = fu.type, "
          + "freeunit.unit = fu.unit "
          + "MERGE (subscription)-[:FREE_UNIT]->(freeunit)) "
          + "FOREACH (ot IN s.order_types | "
          + "MERGE (ordertype:OrderType {id:ot.id}) ON CREATE SET "
          + "ordertype.name = ot.name "
          + "MERGE (subscription)-[:ORDER_TYPE]->(ordertype)) "

          // create extension only when it is present
          + "FOREACH(_ IN CASE WHEN s.extension IS NOT NULL THEN [1] ELSE [] END | "
          + "MERGE (extension:Extension {id:s.id}) SET "
          + "extension.url = s.extension.url, "
          + "extension.tagline = s.extension.tagline, "
          + "extension.roaming_region = s.extension.roaming_region, "
          + "extension.potential_mobile = s.extension.potential_mobile "
          + "MERGE (subscription)-[:EXTENSION]->(extension)) "
          + "WITH s, subscription "
          + "UNWIND s.options as option_id "
          + "MATCH (option:Option {id:option_id}) "
          + "MERGE (subscription)-[:CONTAINS]->(option) ";

  public static final String PROMOTIONS_QUERY =
      "UNWIND {promotions} as p "
          + "MERGE (promotion:Promotion{id: p.id}) ON CREATE SET "
          + "promotion.status = p.status, "
          + "promotion.financial_code = p.financial_code, "
          + "promotion.promotion_family = p.promotion_family, "
          + "promotion.name = p.name, "
          + "promotion.mutation_date = p.mutation_date, "
          + "promotion.value = p.value, "
          + "promotion.mode = p.mode, "
          + "promotion.contract_duration_condition = p.contract_duration_condition, "
          + "promotion.cross_sell = p.cross_sell, "
          + "promotion.handset_bundle = p.handset_bundle, "
          + "promotion.sale_start_date = p.sale_start_date, "
          + "promotion.sale_end_date = p.sale_end_date, "
          + "promotion.duration = p.duration, "
          + "promotion.value_unit = p.value_unit "
          + "FOREACH (ot IN p.order_types | "
          + "MERGE (ordertype:OrderType {id:ot.id}) ON CREATE SET "
          + "ordertype.name = ot.name "
          + "MERGE (promotion)-[:ORDER_TYPE]->(ordertype)) "
          + "WITH p, promotion "
          + "OPTIONAL MATCH (option:Option {id:p.option}) "
          + "FOREACH(x IN (CASE WHEN option IS NULL THEN [] ELSE [1] END) | "
          + "MERGE (promotion)<-[:CONTAINS]-(option))"
          + "WITH p, promotion "
          + "OPTIONAL MATCH (subscription:Subscription {id:p.subscription}) "
          + "FOREACH(x IN (CASE WHEN subscription IS NULL THEN [] ELSE [1] END) | "
          + "MERGE (promotion)<-[:SOLD_UNDER]-(subscription)) "

          // Hardware is not available now. But in the month of december there will be
          // a new business ruling wherein there will be a discount associated with the
          // subscription provided
          // that a particular hardware is chosen. When that happens there will be a
          // hardware list that is populated.
          // At the same time the duration field will also be populated.
          + "WITH p, promotion "
          + "UNWIND COALESCE(p.hardwares, [null]) as hardware_id "
          + "MATCH (hardware:Hardware {id: hardware_id}) "
          + "MERGE (promotion)-[:CONTAINS]->(hardware) ";

  public static final String HARDWARE_QUERY =
      "UNWIND {hardwares} as h "
          + "MERGE (hardware:Hardware{id: h.id}) ON CREATE SET "
          + "hardware.hardware_type = h.hardware_type, "
          + "hardware.network_type = h.network_type, "
          + "hardware.sticky = h.sticky, "
          + "hardware.description = h.description, "
          + "hardware.commercial_product_name = h.commercial_product_name, "
          + "hardware.sale_end_date = h.sale_end_date, "
          + "hardware.guarantee_period = h.guarantee_period, "
          + "hardware.ean_code = h.ean_code, "
          + "hardware.simcard_type = h.simcard_type, "
          + "hardware.sale_start_date = h.sale_start_date, "
          + "hardware.vat_code = h.vat_code, "
          + "hardware.deviating_range = h.deviating_range, "
          + "hardware.hardware_sub_type = h.hardware_sub_type, "
          + "hardware.os = h.os, "
          + "hardware.insurance_category = h.insurance_category "
          + "FOREACH (p IN h.prices | "
          + "MERGE (price:Price {id:p.id}) ON CREATE SET "
          + "price.end_date = p.end_date, "
          + "price.type = p.type, "
          + "price.value = p.value, "
          + "price.start_date = p.start_date, "
          + "price.vat_category = p.vat_category, "
          + "price.value = p.value "
          // hardware item is a reference to this hardware itself. Not including here as
          // it would be implied by the relation.
          + "MERGE (hardware)-[:PRICED_AS]->(price)) ";

  // The subscriptions inside the hardware entity can be totally ignored.
  // In the future there will be a separate rule entity that would describe
  // the discount associated with the subscription provided that a particular
  // hardware is chosen.
  // In either case, the subscription inside the hardware entity can be ignored.

  public static final String COMMERCIAL_GROUP_QUERY =
      "UNWIND {commercial_groups} as c "
          + "MERGE (commercial_group:CommercialGroup{id: c.id}) ON CREATE SET "
          + "commercial_group.parent_group = c.parent_group, "
          + "commercial_group.label = c.label, "
          + "commercial_group.name = c.name";

  public static final String COUNTRY_QUERY =
      "UNWIND {countries} as co "
          + "MERGE (country:Country{id: co.id}) ON CREATE SET "
          + "country.name = co.name, "
          + "country.roaming_region = co.roaming_region, "
          + "country.roaming_bundle_category = co.roaming_bundle_category";
}
