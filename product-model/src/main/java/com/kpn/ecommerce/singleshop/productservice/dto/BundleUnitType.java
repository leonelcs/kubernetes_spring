package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BundleUnitType {

  GB("GB"), VOICE_SMS("bel/sms");

  @Getter
  @JsonValue
  private final String value;

}
