package com.kpn.ecommerce.singleshop.cartservice.cart;

import com.kpn.ecommerce.singleshop.cartservice.client.ProductCatalogueClient;
import com.kpn.ecommerce.singleshop.cartservice.cart.domain.Cart;
import com.kpn.ecommerce.singleshop.productservice.dto.CartDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SubscriptionDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CartServiceImpl {

  @NonNull
  private final ProductCatalogueClient productCatalogueClient;

  //TODO: make a method that does not return a default product so we don't have to check if it is present and add it otherwise
  public CartDto getOptionDataForCart(Cart cart) {
    List<SubscriptionDto> subscriptions = new ArrayList<>();
    CartDto cartDto = new CartDto();
    cart.getCartOptions().stream().forEach(cartOption -> {
      SimOnlyDto tmpSimOnlyDto = productCatalogueClient.getSubscriptionById(cartOption.getId()).getBody();
      if (cartDto.getOneTimeCost() == null) {
        cartDto.setOneTimeCost(tmpSimOnlyDto.getOneTimeCost());
      }
      subscriptions.addAll(tmpSimOnlyDto.getSubscriptions());
    });

    cartDto.setTotalPrice(cartDto.getOneTimeCost() + getTotalSubscriptionsPrice(subscriptions));
    cartDto.setSubscriptions(subscriptions);
    return cartDto;
  }

  private Double getTotalSubscriptionsPrice(List<SubscriptionDto> subscriptions) {
    return subscriptions.stream().mapToDouble(subscription -> subscription.getActualPrice()).sum();
  }
}
