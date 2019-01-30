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
public class DefaultProductDto {

  @ApiModelProperty(notes = "The default duration which will be initially selected when customer didn't select any yet.")
  private ContractDuration duration;

  @ApiModelProperty(notes = "The default completeness (standaard).")
  private CompletenessBenefits.Completeness completeness;

  @ApiModelProperty(notes = "Name of the bundle that is selected by default when customer didn't select any yet.")
  private String defaultBundle;

  @ApiModelProperty(notes = "p{id}:s{id}:... id to filter the product.")
  private String id;

}
