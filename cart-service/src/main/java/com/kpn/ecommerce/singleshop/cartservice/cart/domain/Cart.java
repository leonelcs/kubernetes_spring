package com.kpn.ecommerce.singleshop.cartservice.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.constants.RelationShipConstants;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

  //Developers:
  //Do not ever use the ID in any code. Always use Cart@uuid to do any operation on the cart object.
  @Id
  @GeneratedValue
  @Index(unique = true)
  @JsonIgnore
  private Long id;

  //The uuid is here to communicate our cart to the outside world. We are using it because the Long id is not secure enough to expose.
  //Use the uuid to perform actions on the cart object.
  @JsonProperty(value = "id")
  private String uuid;

  @Relationship(type = RelationShipConstants.CONTAINS)
  private List<CartOption> cartOptions;

  public void addOptions(List<CartOption> cartOptions) {
    this.cartOptions.addAll(cartOptions);
  }
}
