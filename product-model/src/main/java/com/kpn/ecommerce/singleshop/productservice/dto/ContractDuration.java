package com.kpn.ecommerce.singleshop.productservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractDuration {
  ONE_YEAR(12) {
    @JsonValue
    @Override
    public String toString() {
      return "1-jaar";
    }
  },
  TWO_YEAR(24) {
    @JsonValue
    @Override
    public String toString() {
      return "2-jaar";
    }
  };

  @Getter
  private final int months;

  public static ContractDuration fromValue(int months) {
    return Stream.of(ContractDuration.values()).filter(havingContractDuration(months)).findFirst()
        .orElseThrow(error(months));
  }

  private static Predicate<ContractDuration> havingContractDuration(int months) {
    return cd -> cd.getMonths() == months;
  }

  private static Supplier<IllegalArgumentException> error(final int months) {
    return () -> new IllegalArgumentException(String.format("%s is not a valid contract duration.", months));
  }

  public static ContractDuration defaultContractDuration() {
    return ContractDuration.TWO_YEAR;
  }

  @JsonCreator
  public static ContractDuration forValue(String value) {
    if (value.equals(ONE_YEAR.toString())) {
      return ONE_YEAR;
    } else {
      return TWO_YEAR;
    }
  }
}
