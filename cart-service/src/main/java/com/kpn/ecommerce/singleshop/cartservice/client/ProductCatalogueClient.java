package com.kpn.ecommerce.singleshop.cartservice.client;


import com.kpn.ecommerce.singleshop.cartservice.config.FeignClientConfiguration;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "ProductCatalogueClient", url = "${product.service.client.uri}", configuration = FeignClientConfiguration.class)
public interface ProductCatalogueClient {

  @GetMapping(value = "/simonly/subscription/{id}")
  ResponseEntity<SimOnlyDto> getSubscriptionById(@PathVariable(value = "id") final String id);
}
