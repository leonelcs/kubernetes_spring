package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.time.LocalDateTime;
import java.util.Set;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "hardware")
public class HardWare {

  @Id
  @GeneratedValue
  private Long _id;

  private String id;
  private String hardwareType;
  private String networkType;
  private boolean sticky;
  private String description;
  private String commercialProductName;
  private int guaranteePeriod;
  private String eanCode;
  private String simCardType;
  private String vatCode;
  private String deviatingRange;
  private String hardwareSubType;
  private String os;
  @Relationship(type = RelationShipConstants.CONTAINS)
  private Set<Option> options;
  @Relationship(type = RelationShipConstants.SELLS)
  private Set<Subscription> subscriptions;
  @Relationship(type = RelationShipConstants.PRICED)
  private Set<Price> prices;
  private String insuranceCategory;
  private LocalDateTime saleStartDate;
  private LocalDateTime saleEndDate;

}
