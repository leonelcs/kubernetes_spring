package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartDto {

    @ApiModelProperty(notes = "The subscriptions in the cart.")
    private List<SubscriptionDto> subscriptions;

    @ApiModelProperty(notes = "Total price of all items in cart.")
    public Double totalPrice;

    @ApiModelProperty(notes = "One time payment to purchase the subscription")
    public Double oneTimeCost;
}
