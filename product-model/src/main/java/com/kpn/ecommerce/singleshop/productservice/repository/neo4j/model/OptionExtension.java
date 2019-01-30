package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "optionextension")
public class OptionExtension {

  @Id
  @GeneratedValue
  private Long id;
  private String url;
  private String tagline;
  private String commercialGroupName;
  private String commercialGroup;
  private int uiSort;
  private int mutateUntilDays;
  private int mutateAfterDays;
  private boolean mutateNever;
  private boolean mutateUntil;
  private boolean mutateAfter;
  private boolean addon;
  private boolean display;
  private String commercialGroupLabel;
}
