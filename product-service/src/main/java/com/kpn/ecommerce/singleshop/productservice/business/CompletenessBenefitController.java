package com.kpn.ecommerce.singleshop.productservice.business;

import static java.util.Arrays.asList;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Product;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
class CompletenessBenefitController {

  static final String AVAILABLE_PRODUCTS = "/available_products";
  static final String COMPLETENESS = "/completeness";
  static final String COMPLETE_BENEFITS = "/complete-benefits";


  @GetMapping(value = AVAILABLE_PRODUCTS, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
  ResponseEntity<List<Product>> availableProducts() {
    log.debug("retrieved request for available products.");
    return ResponseEntity.ok(asList(Product.values()));
  }

  @GetMapping(value = COMPLETENESS, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
  ResponseEntity<List<Completeness>> completeness() {
    log.debug("retrieved get request for completeness.");
    return ResponseEntity.ok(asList(Completeness.values()));
  }


  @PostMapping(value = COMPLETE_BENEFITS, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
  ResponseEntity<CompletenessBenefits> completeBenefits(
      @RequestParam(required = false) final List<Product> products) {
    log.debug("retrieved request for completeBenefits, [{}].", products);
    final CompletenessBenefits completeness = CompletenessProcessor.determineByProducts(products);
    return ResponseEntity.ok(completeness);
  }
}
