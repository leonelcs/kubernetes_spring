package com.kpn.ecommerce.singleshop.productservice.simonly;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.LineUp;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.SubscriptionBundle;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimOnlyServiceTest {

  private static final String FIRST_ID = "p1090537502:s751358035";
  private static final String SECOND_ID = "p590538506:s751358035";
  private static final String AMOUNT = "amount";
  private static final String DATA = "data";
  private static final String ID = "id";
  private static final CompletenessBenefits COMPLETENESS_BENEFITS = new CompletenessBenefits(
      Completeness.DEFAULT, Completeness.DEFAULT.benefits());
  private static final String KPN_ZORGELOOS_STANDAARD = "KPN Zorgeloos Standaard";
  private static final String DESCRIPTION = "description";
  private static final String ONBEPERKT = "Onbeperkt";
  private static final String BEL_SMS = "bel/sms";
  private static final String VOICE_SMS = "voice_sms";
  private static final String GB = "GB";
  private static final String COMB = "comb";

  @Mock
  private SubscriptionBundleRepository repository;

  @Mock
  private SimOnlyMapper mapper;

  @Mock
  private SubscriptionRepository subscriptionRepository;

  private SimOnlyService objectUnderTest;

  @Before
  public void before() {
    initMocks(this);
    final List<SubscriptionBundle> bundleList = createBundle();

    when(repository.findAllRelevantSubscriptionBundles(LineUp.KPN_MOBILE_2017.toString()))
        .thenReturn(bundleList);
    SimOnlyDto simOnlyDto = createExpectedSimOnlyDto(bundleList);
    when(mapper
        .toResponse(COMPLETENESS_BENEFITS, bundleList)).thenReturn(simOnlyDto);
    objectUnderTest = new SimOnlyServiceImpl(repository, mapper, subscriptionRepository);

  }

  private SimOnlyDto createExpectedSimOnlyDto(final List<SubscriptionBundle> bundleList) {
    SimOnlyMapper mapper = new SimOnlyMapper(1000L,
        2000L, Double.valueOf("25.0"));
    return mapper.toResponse(COMPLETENESS_BENEFITS, bundleList);
  }

  @Test
  public void testFindSimOnlySubscriptionsByFilter() {
    List<SubscriptionBundle> listBundle = createBundle();
    SimOnlyDto expected = createExpectedSimOnlyDto(listBundle);

    SimOnlyDto simOnlyDto = objectUnderTest.findSimOnlySubscriptionsByFilter(COMPLETENESS_BENEFITS);

    assertThat(simOnlyDto.getOneTimeCost(), is(equalTo(expected.getOneTimeCost())));
    assertThat(simOnlyDto.getDefaultSubscriptionId(),
        is(equalTo(expected.getDefaultSubscriptionId())));

    assertThat(simOnlyDto.getSubscriptions(), allOf(
        hasItem(
            both(hasProperty(DATA, hasProperty(AMOUNT, is("10"))))
                .and(hasProperty(ID, is(FIRST_ID)))
        ),
        hasItem(
            both(hasProperty(DATA, hasProperty(AMOUNT, is("20"))))
                .and(hasProperty(ID, is(SECOND_ID)))
        )
    ));
  }

  private static List<SubscriptionBundle> createBundle() {
    return asList(new SubscriptionBundle(
            FIRST_ID,
            KPN_ZORGELOOS_STANDAARD,
            45.0,
            12,
            DESCRIPTION,
            31.00,
            asList(
                asList("10", GB, DATA), // String
                asList("0", BEL_SMS, COMB), // Integer
                asList(ONBEPERKT, BEL_SMS, VOICE_SMS) // String
            )),
        new SubscriptionBundle(
            SECOND_ID,
            KPN_ZORGELOOS_STANDAARD,
            45.0,
            24,
            DESCRIPTION,
            30.00,
            asList(
                asList("20", GB, DATA), // String
                asList("0", BEL_SMS, COMB), // Integer
                asList(ONBEPERKT, BEL_SMS, VOICE_SMS) // String
            )));
  }

}
