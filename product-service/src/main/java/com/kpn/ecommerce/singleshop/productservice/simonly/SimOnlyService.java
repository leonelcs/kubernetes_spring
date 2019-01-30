package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import java.util.Optional;

interface SimOnlyService {

  SimOnlyDto findSimOnlySubscriptionsByFilter(final CompletenessBenefits completenessBenefits);

  Optional<SimOnlyDto> findSimOnlySubscriptionById(String id);
}
