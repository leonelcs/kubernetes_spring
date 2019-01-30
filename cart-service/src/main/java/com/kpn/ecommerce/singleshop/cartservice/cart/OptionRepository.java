package com.kpn.ecommerce.singleshop.cartservice.cart;

import com.kpn.ecommerce.singleshop.cartservice.cart.domain.CartOption;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface OptionRepository extends Neo4jRepository<CartOption, Long> {

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  CartOption findById(String externalId);
}

