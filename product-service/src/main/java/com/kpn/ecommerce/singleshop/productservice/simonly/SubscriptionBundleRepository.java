package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model.Promotion;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.SubscriptionBundle;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface SubscriptionBundleRepository extends Neo4jRepository<Promotion, Long> {

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  Iterable<SubscriptionBundle> findAllRelevantSubscriptionBundles(@Param("lineUpValue") final String lineUpValue);

  @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
  Iterable<SubscriptionBundle> findAllRelevantSubscriptionBundlesById(@Param("promotionId") final String promotionId,
      @Param("subscriptionId") final String subscriptionId);
}
