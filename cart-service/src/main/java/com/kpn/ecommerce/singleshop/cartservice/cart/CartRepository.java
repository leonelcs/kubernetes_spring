package com.kpn.ecommerce.singleshop.cartservice.cart;

import com.kpn.ecommerce.singleshop.cartservice.cart.domain.Cart;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CartRepository extends Neo4jRepository<Cart, Long> {
  Optional<Cart> findByUuid(String id);
}
