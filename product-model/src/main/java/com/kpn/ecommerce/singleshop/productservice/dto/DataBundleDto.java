package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class DataBundleDto {

  @ApiModelProperty(notes = "The amount of data of the bundle")
  private String amount;

  @ApiModelProperty(notes = "The units used to represent Data, Minutes or SMS",
      example = "GB")
  private BundleUnitType unit;

}
