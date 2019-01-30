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

@NodeEntity(label = "option")
@CompositeIndex(properties = {"id", "name"})
@Data
public class Option {

  @Id
  @GeneratedValue
  private Long _id;
  @Index(unique = true)
  private String id;
  private String status;
  private String chargeType;
  private String name;
  private String financialCode;
  private String marketCode;
  private String contractDurationCondition;
  private String offerType;
  private String insuranceCategory;
  private BigDecimal listPriceExclVat;
  private BigDecimal listPriceInclVat;
  private LocalDateTime mutationDate;
  private LocalDateTime saleStartDate;
  private LocalDateTime saleEndDate;
  @Relationship(type = RelationShipConstants.FREE_UNITS, direction = Relationship.INCOMING)
  private Set<FreeUnit> freeunits;
  @Relationship(type = RelationShipConstants.ORDER_TYPE, direction = Relationship.INCOMING)
  private Set<OrderType> orderTypes;
  @Relationship(type = RelationShipConstants.EXTENSION)
  private OptionExtension extension;
  private boolean mbTransfer;
  private String type;

}
