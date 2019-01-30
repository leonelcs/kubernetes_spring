package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel(description = "The Subscription object holds all the attributes related to the"
    + " mobile bundle (or plans) like internet bundle, prices and related information")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {

  @ApiModelProperty(notes = "this id is a representation of how the information can be "
      + "found in the Digital Engine structure"
      + "\n \t - p{id}:s{id}:h{id}:o{id}:g{id}"
      + "\n \t p = promotions - used to calculate discount prices and extra features"
      + "\n \t s = subscriptions - the mobile bundle itself (postpaid plan for example)"
      + "\n \t h = hardwares - handsets - optional"
      + "\n \t o = options - extra non-excludent options"
      + "\n \t g = options_groups - extra excludent options"
      + "\n \t |customer_type <- the customer type (kpn compleet) is separated by | (pipe)",
      example = "p1391230623:s1951363155|standaard")
  private String id;

  @ApiModelProperty(notes = "Full name of the subscription, usually KPN is not exibited in the presentation layer ")
  private String name;

  @ApiModelProperty(notes = "generated no-spaces-string to be used in the URL")
  private String slug;

  @ApiModelProperty(notes = "generated no-spaces-string to be used in the URL like the slug")
  private ContractDuration duration;

  @ApiModelProperty(notes = "Full price of the subscriptions, usually are only used as a reference"
      + " and comes with a stripe over it")
  private Double listPrice;

  @ApiModelProperty(notes = "calculated price after the discount")
  private Double actualPrice;

  @ApiModelProperty(notes = "Amount of data (internet) of this subscription")
  private DataBundleDto data;

  @ApiModelProperty(notes = "Amount of minutes and sms messages of this subscription")
  private VoiceSmsBundleDto voiceSmsNational;

  @ApiModelProperty(notes = "Amount of data (internet) of this subscription outside the Europe Union")
  private DataBundleDto dataRoaming;

}
