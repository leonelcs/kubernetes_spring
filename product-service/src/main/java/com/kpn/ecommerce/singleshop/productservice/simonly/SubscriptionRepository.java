package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model.Subscription;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface SubscriptionRepository extends Neo4jRepository<Subscription, Long> {

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  Optional<Subscription> findById(String id);
}
