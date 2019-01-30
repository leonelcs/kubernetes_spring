package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "subscriptionextension")
public class SubscriptionExtension {

  @Id
  @GeneratedValue
  private Long id;

  private String url;
  private String tagline;
  private String roamingRegion;
  private boolean potentialMobile;
}
