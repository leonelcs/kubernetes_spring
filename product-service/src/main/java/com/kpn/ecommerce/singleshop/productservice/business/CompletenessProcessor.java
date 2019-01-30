package com.kpn.ecommerce.singleshop.productservice.business;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Benefit;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletenessProcessor {

  private static final String PREFIX = "[";
  private static final String SUFFIX = "]";
  private static final String DELIMITER = ",";

  @SuppressWarnings("unused")
  static int numberOfMobileSubscriptions(final List<Product> products) {
    // get a map of Products with their respective number of occurrences in the list.
    final Map<Product, Long> counted = products.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    // determine the number of Product.MOBILE_PHONE's in the list, this get's the total discount.
    return counted.entrySet().stream()
        .filter(p -> p.getValue() > 1 && p.getKey() == Product.MOBILE_PHONE).map(Entry::getValue)
        .findFirst().orElse(0L).intValue();
  }


  public static CompletenessBenefits determineByProducts(final List<Product> products) {
    final List<Product> suppliedProducts = Optional.ofNullable(products)
        .orElse(Collections.emptyList());
    final Set<Product> withoutDuplicates = new HashSet<>(suppliedProducts);

    // multiple Completeness can occur. VAMO + MOBILE
    final List<Completeness> completenessList = Stream.of(Completeness.values())
        .filter(containsAllProductsForThisCompleteness(withoutDuplicates))
        .filter(completeness -> completeness != Completeness.DEFAULT)
        .collect(Collectors.toList());

    // grab all benefits from multiple completeness(es) and remove duplicates
    final Set<Benefit> benefits = completenessList.stream()
        .flatMap(
            (Function<Completeness, Stream<Benefit>>) completeness -> completeness.benefits()
                .stream()).collect(Collectors.toSet());

    // completeness is ordered from most to least benefits, take the entry with most benefits.
    final Completeness completeness = completenessList.stream().findFirst()
        .orElse(Completeness.DEFAULT); //remove default cause that will always apply.

    if (log.isDebugEnabled()) {
      final StringJoiner productsString = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
      suppliedProducts.forEach(p -> {
        productsString.add(p.name());
      });

      final StringJoiner benefitsString = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
      for (Benefit benefit : benefits) {
        benefitsString.add(benefit.description());
      }
      log.debug("selected product(s): {} results in the following benefits: {}",
          productsString.toString(), benefitsString.toString());
    }
    return new CompletenessBenefits(completeness, new ArrayList<>(benefits));
  }



  private static Predicate<Completeness> containsAllProductsForThisCompleteness(
      final Set<Product> products) {
    return completeness -> Objects.nonNull(products) && completeness
        .containsAllRequiredProducts(products);
  }

  public static int bundleMultiplier(final CompletenessBenefits completenessBenefits) {
    return completenessBenefits.getCompleteness().hasBundleMultiplier() ? 2 : 1;
  }

  public static Double discount(final CompletenessBenefits completenessBenefits) {
    return completenessBenefits.getCompleteness().hasMobileDiscount() ? 5.0 : 0.0;
  }


}
