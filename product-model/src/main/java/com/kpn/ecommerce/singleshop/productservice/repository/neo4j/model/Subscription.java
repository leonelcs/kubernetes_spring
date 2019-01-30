package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "subscription")
@CompositeIndex(properties = {"id", "name"})
@Data
public class Subscription {

  @Id
  @GeneratedValue
  private Long _id;
  @Index(unique = true)
  private String id;
  private String name;
  private String lineup;
  private String portfolio;
  private String subscriptionType;
  private String status;
  private String financialCode;
  private String mobileNumberType;
  private String hardwareSubtype;
  private String brand;
  private BigDecimal listPriceExclVat;
  private BigDecimal listPriceInclVat;
  private LocalDateTime saleStartDate;
  private LocalDateTime saleEndDate;
  private LocalDateTime mutationDate;
  private boolean mbTransfer;
  private boolean postpaid;
  @Relationship(type = RelationShipConstants.EXTENSION)
  private SubscriptionExtension extension;
  @Relationship(type = RelationShipConstants.CONTAINS)
  private Set<Option> options;
  @Relationship(type = RelationShipConstants.OPTION_GROUP)
  private Set<OptionGroup> optionGroups;
  @Relationship(type = RelationShipConstants.DISCOUNT)
  private Set<Discount> discounts;
  @Relationship(type = RelationShipConstants.ORDER_TYPE)
  private Set<OrderType> orderTypes;
  @Relationship(type = RelationShipConstants.FREE_UNITS)
  private Set<FreeUnit> freeunits;
  @Relationship(type = RelationShipConstants.CONTRACT_DURATION)
  private Set<ContractDuration> contractDurations;
  @Relationship(type = RelationShipConstants.MRCS)
  private Set<Mrcs> mrcs;


}
