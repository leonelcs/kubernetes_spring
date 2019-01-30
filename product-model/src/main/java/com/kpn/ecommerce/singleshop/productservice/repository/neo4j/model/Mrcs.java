package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import java.math.BigDecimal;
import java.util.Set;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "mrcs")
public class Mrcs {

  @Id
  @GeneratedValue
  private Long _id;

  @Index(unique = true)
  private String id;
  private BigDecimal maxValue;
  private BigDecimal minValue;
  private String contractDurationCondition;
  private Set<String> deviationAcquisition;
  private Set<String> acquisition;
  private Set<String> retention;
  private String channel;

}
