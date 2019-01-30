package com.kpn.ecommerce.singleshop.productservice.dto;

import static java.util.Arrays.asList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class CompletenessBenefits {

  private Completeness completeness;
  private List<Benefit> benefits;

  @RequiredArgsConstructor
  public enum Completeness implements Descriptor {

    // order is important!!, start from most benefits, to least benefits.
    VAMOTVLANDLINE(EnumSet.of(Product.INTERNET, Product.INTERACTIVE_TV, Product.FIXED_PHONE),
        new Benefit[]{Benefit.MOBILE_DISCOUNT, Benefit.BUNDLE_MULTIPLIER, Benefit.FREE_TV_PACKAGE,
            Benefit.FREE_CALLING}) {
      @JsonValue
      @Override
      public String description() {
        return "vamotvlandline";
      }
    },

    VAMOTV(EnumSet.of(Product.INTERNET, Product.INTERACTIVE_TV),
        new Benefit[]{Benefit.MOBILE_DISCOUNT, Benefit.BUNDLE_MULTIPLIER,
            Benefit.FREE_TV_PACKAGE}) {
      @JsonValue
      @Override
      public String description() {
        return "vamotv";
      }
    },

    VAMO(EnumSet.of(Product.INTERNET),
        new Benefit[]{Benefit.MOBILE_DISCOUNT, Benefit.BUNDLE_MULTIPLIER}) {
      @JsonValue
      @Override
      public String description() {
        return "vamo";
      }
    },

    MOMO(EnumSet.of(Product.MOBILE_PHONE),
        new Benefit[]{Benefit.MOBILE_DISCOUNT, Benefit.MOBILE_DISCOUNT, Benefit.FREE_CALLING,
            Benefit.DATA_BUNDLE_SHARING}) {
      @JsonValue
      @Override
      public String description() {
        return "momo";
      }
    },

    DEFAULT(EnumSet.noneOf(Product.class), new Benefit[]{}) {
      @JsonValue
      @Override
      public String description() {
        return "standaard";
      }
    };

    @JsonCreator
    public static Completeness forValue(final String value) {
      Assert.notNull(value, "value is a mandatory arg.");
      for (Completeness completeness : Completeness.values()) {
        if (value.equals(completeness.description())) {
          return completeness;
        }
      }
      return Completeness.DEFAULT;
    }

    private final Set<Product> productSet;
    private final Benefit[] benefits;

    public boolean containsAllRequiredProducts(final Set<Product> products) {
      return products.containsAll(productSet);
    }

    public List<Benefit> benefits() {
      return Collections.unmodifiableList(asList(benefits));
    }

    public boolean hasBundleMultiplier() {
      return benefits().contains(Benefit.BUNDLE_MULTIPLIER);
    }

    public boolean hasMobileDiscount() {
      return benefits().contains(Benefit.MOBILE_DISCOUNT);
    }
  }

  public enum Product {
    NONE, INTERNET, INTERACTIVE_TV, FIXED_PHONE, MOBILE_PHONE
  }

  public enum Benefit implements Descriptor {
    MOBILE_DISCOUNT {
      @Override
      public String description() {
        return "€5 discount on the mobile subscription.";
      }
    },
    FREE_CALLING {
      @Override
      public String description() {
        return "free calling between both numbers.";
      }
    },
    DATA_BUNDLE_SHARING {
      @Override
      public String description() {
        return "MB sharing between both numbers.";
      }
    },
    BUNDLE_MULTIPLIER {
      @Override
      public String description() {
        return "double data, text, minutes on the mobile subscription.";
      }
    },
    FREE_TV_PACKAGE {
      @Override
      public String description() {
        return "free TV package (FOX Sports Eredivisie or Pluspakket).";
      }
    };
  }
}
