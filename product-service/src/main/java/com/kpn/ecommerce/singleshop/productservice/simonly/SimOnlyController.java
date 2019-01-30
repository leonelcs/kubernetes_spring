package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.business.CompletenessProcessor;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Product;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://singleshop-webapp.main.tcloud.kpn.org",
    "https://singleshop-webapp-dev.main.tcloud.kpn.org",
    "https://singleshop-webapp-tst.main.tcloud.kpn.org",
    "https://singleshop-webapp-acc.main.tcloud.kpn.org"})
class SimOnlyController {

  static final String SIMONLY_SUBSCRIPTIONS = "/simonly/subscriptions";
  @NonNull
  private final SimOnlyService simOnlyService;

  @SuppressWarnings("unused")
  @ApiOperation(value = "fetch simonly subscriptions", notes = "Return the Sim Only bundles")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved the Sim Only subscriptions"),
      @ApiResponse(code = 500, message = "Internal server error")})
  @GetMapping(value = SIMONLY_SUBSCRIPTIONS, produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
  ResponseEntity<SimOnlyDto> simOnly(
      @RequestParam(required = false) final List<Product> completeProducts) {
    log.debug("received sim only GET request.");
    final CompletenessBenefits completenessBenefits = CompletenessProcessor
        .determineByProducts(completeProducts);
    SimOnlyDto simOnly = simOnlyService
        .findSimOnlySubscriptionsByFilter(completenessBenefits);
    log.debug("return json {}", simOnly);
    return ResponseEntity.ok(simOnly);
  }


  //This method is being used in ProductCatalogueClient
  //Because we are using the exact same representation of the subscription we will return a SimOnlyDto
  @ApiOperation(value = "fetch subscription", notes = "Return the Subscription by ID.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved the subscription"),
      @ApiResponse(code = 500, message = "Internal server error"),
      @ApiResponse(code = 404, message = "Subscription not found")})
  @ResponseBody
  @GetMapping(value = "/simonly/subscription/{id}")
  public ResponseEntity<SimOnlyDto> getSubscriptionById(
      @PathVariable(value = "id") final String id) {
    log.debug("Fetching subscription with id: [{}].", id);
    final Optional<SimOnlyDto> optionalSimOnlyDto = simOnlyService.findSimOnlySubscriptionById(id);
    return optionalSimOnlyDto.map(simOnlyDto -> ResponseEntity.ok().body(simOnlyDto))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
