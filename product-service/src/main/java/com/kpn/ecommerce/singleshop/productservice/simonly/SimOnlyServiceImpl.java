package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.LineUp;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.SubscriptionBundle;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class SimOnlyServiceImpl implements SimOnlyService {

  @NonNull
  private final SubscriptionBundleRepository subscriptionBundleRepository;

  @NonNull
  private final SimOnlyMapper simOnlyMapper;

  @NonNull
  private final SubscriptionRepository subscriptionRepository;

  @Override
  public SimOnlyDto findSimOnlySubscriptionsByFilter(final CompletenessBenefits completenessBenefits) {
    log.debug("findSimOnlySubscriptionsByFilter using completeness {}", completenessBenefits);
    final Iterable<SubscriptionBundle> subscriptionsBundles =
        subscriptionBundleRepository.findAllRelevantSubscriptionBundles(LineUp.KPN_MOBILE_2017.toString());
    return simOnlyMapper.toResponse(completenessBenefits, subscriptionsBundles);
  }

  @Override
  public Optional<SimOnlyDto> findSimOnlySubscriptionById(String id) {
    String promotionId = StringUtils.substringBetween(id, "p", ":");
    String subscriptionId = StringUtils.substringAfter(id, ":s");
    Iterable<SubscriptionBundle> subscriptionsBundles =
        subscriptionBundleRepository.findAllRelevantSubscriptionBundlesById(promotionId, subscriptionId);
    SimOnlyDto simOnlyDto = simOnlyMapper
        .toResponse(new CompletenessBenefits(Completeness.DEFAULT, Completeness.DEFAULT.benefits()),
            subscriptionsBundles);
    if (simOnlyDto.getSubscriptions().isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(simOnlyDto);
  }


}
