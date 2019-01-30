package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "promotion")
@CompositeIndex(properties = {"id", "name"})
public class Promotion {

  @Id
  @GeneratedValue
  private Long _id;
  private String id;
  private String name;
  private String status;
  private String financialCode;
  private String promotionFamily;
  private LocalDateTime mutationDate;
  private LocalDateTime saleStartDate;
  private LocalDateTime saleEndDate;
  private BigDecimal value;
  private String mode;
  private String contractDurationCondition;
  private String crossSell;
  private Set<HardWare> hardwares;
  private String handsetBundle;
  private int duration;
  @Relationship(type = RelationShipConstants.SOLD_UNDER, direction = Relationship.INCOMING)
  private Subscription subscription;
  private String valueUnit;
  @Relationship(type = RelationShipConstants.CONTAINS, direction = Relationship.INCOMING)
  private Option option;
  @Relationship(type = RelationShipConstants.ORDER_TYPE)
  private Set<OrderType> orderTypes;
}
