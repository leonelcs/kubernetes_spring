package com.kpn.ecommerce.singleshop.productservice.simonly.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionBundle {

  private String id;
  private String name;
  private Double listPrice;
  @JsonIgnore
  private Integer duration; // duration in months
  @JsonIgnore
  private String description;
  private Double actualPrice;
  @JsonIgnore
  private List<List<String>> bundles;

}
