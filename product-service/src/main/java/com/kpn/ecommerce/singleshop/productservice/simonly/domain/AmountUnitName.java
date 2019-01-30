package com.kpn.ecommerce.singleshop.productservice.simonly.domain;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class AmountUnitName {

  private final String amount;
  private final String unit;
  private final Bundle bundle;

  public static AmountUnitName of(final List<String> args) {
    Assert.notEmpty(args, "empty argument.");
    Assert.isTrue(args.size() == 3, "argument does not contain 3 entries");
    return new AmountUnitName(args.get(0), args.get(1), Bundle.valueOf(args.get(2).toUpperCase()));
  }
}
