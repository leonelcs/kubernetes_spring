package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "price")
@CompositeIndex(properties = {"id", "type"})
public class Price {

  @Id
  @GeneratedValue
  private Long _id;
  private String type;
  private BigDecimal value;
  private String vatCategory;
  private String id;
  private String hardwareItem;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
