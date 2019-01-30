package com.kpn.ecommerce.singleshop.cartservice.cart;

import com.kpn.ecommerce.singleshop.cartservice.cart.domain.Cart;
import com.kpn.ecommerce.singleshop.productservice.dto.CartDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://singleshop-webapp.main.tcloud.kpn.org",
    "https://singleshop-webapp-dev.main.tcloud.kpn.org", "https://singleshop-webapp-tst.main.tcloud.kpn.org",
    "https://singleshop-webapp-acc.main.tcloud.kpn.org"})
class CartController {

  public static final String ID = "id";
  public static final String ID_VARIABLE = "/{id}";

  @NonNull
  private final CartRepository cartRepository;

  @Autowired
  private CartServiceImpl cartServiceImpl;

  @ApiOperation(value = "Get cart", notes = "Return the cart and it's content based on ID.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved the subscription"),
      @ApiResponse(code = 500, message = "Internal server error"),
      @ApiResponse(code = 404, message = "Cart not found")})
  @GetMapping(ID_VARIABLE)
  ResponseEntity<CartDto> cartContent(@PathVariable(value = ID) final String uuid) {
    log.debug("Fetching cart options with id: [{}].", uuid);
    final Optional<CartDto> optionalCartDto =
        cartRepository.findByUuid(uuid).map(cart -> cartServiceImpl.getOptionDataForCart(cart));
    return optionalCartDto.map(cartDto -> {
      log.debug("Cart with id: [{}] found.", uuid);
      return ResponseEntity.ok().body(cartDto);
    }).orElseGet(() -> {
      log.debug("No cart found for id: [{}].", uuid);
      return ResponseEntity.notFound().build();
    });
  }

  @ApiOperation(value = "Put cart", notes = "Update the content of a cart or create a new one if it's ID does not exists.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated the cart, or created a new one."),
      @ApiResponse(code = 500, message = "Internal server error"),
      @ApiResponse(code = 404, message = "Cart not found")})
  @PutMapping
  ResponseEntity<Cart> updateCart(@RequestBody @Valid Cart currentCart) {
    log.debug("Updating cart with id: [{}].", currentCart.getUuid());
    Optional<Cart> optionalCart = cartRepository.findByUuid(currentCart.getUuid());
    return optionalCart.map(cart -> {
      log.debug("Cart with id: [{}] found. Updating options.", cart.getUuid());
      cart.addOptions(currentCart.getCartOptions());
      cartRepository.save(cart);
      log.debug("Cart with id: [{}] options succefully updated.", cart.getUuid());
      return ResponseEntity.ok(cart);
    }).orElseGet(() -> {
      log.debug("No cart found for id: [{}]. Options could not be updated. Returning a new Cart.",
          currentCart.getUuid());
      return saveCart(currentCart);
    });
  }

  @ApiOperation(value = "Post cart", notes = "Creates a new cart with the passed options.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully created the cart"),
      @ApiResponse(code = 500, message = "Internal server error")})
  @PostMapping
  ResponseEntity<Cart> saveCart(@RequestBody @Valid Cart cart) {
    log.debug("Received cart save request.");
    cart.setUuid(UUID.randomUUID().toString());
    cartRepository.save(cart);
    log.debug("New cart item saved to database, the new cart id is: [{}].", cart.getUuid());
    return ResponseEntity.ok().body(cart);
  }
}
