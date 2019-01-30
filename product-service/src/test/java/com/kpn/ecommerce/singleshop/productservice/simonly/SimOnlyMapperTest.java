package com.kpn.ecommerce.singleshop.productservice.simonly;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.hasProperty;

import com.kpn.ecommerce.singleshop.productservice.dto.BundleUnitType;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.ContractDuration;
import com.kpn.ecommerce.singleshop.productservice.dto.DataBundleDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SubscriptionDto;
import com.kpn.ecommerce.singleshop.productservice.dto.VoiceSmsBundleDto;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.SubscriptionBundle;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

public class SimOnlyMapperTest {

  private static final String UNLIMITED = "onbeperkt";
  private static final String FIRST_ID = "p1000:s2000";

  private static final String AMOUNT = "amount";
  private static final String DATA = "data";
  private static final String ID = "id";
  private static final String KPN_ZORGELOOS_STANDAARD = "KPN Zorgeloos Standaard";

  private SimOnlyMapper objectUnderTest;

  @Before
  public void setup() {
    objectUnderTest = new SimOnlyMapper(1000L,
        2000L, Double.valueOf("25.0"));
  }


  @Test
  public void toResponseHappyFlow() {
    //create a bundle
    List<SubscriptionBundle> subscriptionsBundles = Lists.list(createBundle());

    List<SubscriptionDto> listDto = createSubscriptionsDto();

    SimOnlyDto expected = new SimOnlyDto(25.0, FIRST_ID, listDto,
        new CompletenessBenefits(Completeness.DEFAULT, Completeness.DEFAULT.benefits()));

    //convert the bundle
    SimOnlyDto dto = objectUnderTest
        .toResponse(new CompletenessBenefits(Completeness.DEFAULT, Completeness.DEFAULT.benefits()),
            subscriptionsBundles);

    assertThat(dto.getOneTimeCost(), is(equalTo(expected.getOneTimeCost())));
    assertThat(dto.getDefaultSubscriptionId(), is(equalTo(expected.getDefaultSubscriptionId())));
    assertThat(dto.getSubscriptions(), hasItem(
        both(hasProperty(DATA, hasProperty(AMOUNT, is("10"))))
            .and(hasProperty(ID, is(FIRST_ID)))
    ));

    //convert the bundle
    dto = objectUnderTest
        .toResponse(new CompletenessBenefits(Completeness.VAMO, Completeness.VAMO.benefits()),
            subscriptionsBundles);

    assertThat(dto.getOneTimeCost(), is(equalTo(expected.getOneTimeCost())));
    assertThat(dto.getDefaultSubscriptionId(), is(equalTo(expected.getDefaultSubscriptionId())));
    assertThat(dto.getSubscriptions(), hasItem(
        both(hasProperty(DATA, hasProperty(AMOUNT, is("20"))))
            .and(hasProperty(ID, is(FIRST_ID)))
        )
    );
  }

  private List<SubscriptionDto> createSubscriptionsDto() {
    return Lists.list(SubscriptionDto.builder()
            .id(FIRST_ID)
            .listPrice(45.0)
            .actualPrice(31.0)
            .duration(ContractDuration.ONE_YEAR)
            .name(KPN_ZORGELOOS_STANDAARD)
            .slug("zorgeloos-standaard")
            .data(new DataBundleDto("10", BundleUnitType.GB))
            .voiceSmsNational(new VoiceSmsBundleDto(UNLIMITED, BundleUnitType.VOICE_SMS)).build(),
        SubscriptionDto.builder()
            .id(FIRST_ID)
            .data(new DataBundleDto("20", BundleUnitType.GB))
            .actualPrice(26.0)
            .build());
  }

  private SubscriptionBundle createBundle() {
    return new SubscriptionBundle(
        FIRST_ID,
        KPN_ZORGELOOS_STANDAARD,
        45.0,
        12,
        "description",
        31.00,
        asList(
            asList("10", "GB", DATA), // String
            asList("0", "bel/sms", "comb"), // Integer
            asList("Onbeperkt", "bel/sms", "voice_sms") // String
        ));
  }

}
