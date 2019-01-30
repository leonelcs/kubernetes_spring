package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.time.LocalDateTime;
import java.util.Set;
import org.neo4j.ogm.annotation.CompositeIndex;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "optiongroup")
@CompositeIndex(properties = {"id", "name"})
public class OptionGroup {

  @Id
  @GeneratedValue
  private Long _id;

  @Index(unique = true)
  private String id;

  private String status;
  private String name;
  private int minSelectOffer;
  private int maxSelectOffer;
  private LocalDateTime mutationDate;
  private LocalDateTime saleStartDate;
  private LocalDateTime saleEndDate;
  @Relationship(type = RelationShipConstants.CONTAINS)
  private Set<Option> options;
}
