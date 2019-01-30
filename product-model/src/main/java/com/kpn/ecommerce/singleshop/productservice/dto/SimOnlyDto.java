package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class SimOnlyDto {

  @ApiModelProperty(notes = "One time payment to purchase the subscription.")
  private Double oneTimeCost;

  @ApiModelProperty(notes = "p{id}:s{id} default Product to be shown on the initial page.")
  private String defaultSubscriptionId;

  @ApiModelProperty(notes = "List of possible bundles")
  private List<SubscriptionDto> subscriptions;

  @ApiModelProperty(notes = "Shows the completeness and associated benefits in the current configuration.")
  private CompletenessBenefits completenessBenefits;

}
