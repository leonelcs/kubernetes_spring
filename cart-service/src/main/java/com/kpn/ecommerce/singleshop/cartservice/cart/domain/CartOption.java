package com.kpn.ecommerce.singleshop.cartservice.cart.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "cartoption")
@Data
@NoArgsConstructor
public class CartOption {

  @Id
  @Index
  private String id;

}
