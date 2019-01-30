package com.kpn.ecommerce.singleshop.productservice.simonly;

import static com.kpn.ecommerce.singleshop.productservice.simonly.SimOnlyController.SIMONLY_SUBSCRIPTIONS;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kpn.ecommerce.singleshop.productservice.dto.BundleUnitType;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Product;
import com.kpn.ecommerce.singleshop.productservice.dto.ContractDuration;
import com.kpn.ecommerce.singleshop.productservice.dto.DataBundleDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SubscriptionDto;
import com.kpn.ecommerce.singleshop.productservice.dto.VoiceSmsBundleDto;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class SimOnlyControllerTest {

  private static final String PARAMETER = "completeProducts";
  private static final String ID = "p1000:s2000";
  private static final String COMPLETENESS = "$.completeness_benefits.completeness";
  private static final String BENEFITS = "$.completeness_benefits.benefits";
  private static final String SUBSCIPTION_ID = "id";


  private static SimOnlyDto createSimOnlyDto(final CompletenessBenefits completenessBenefits) {
    return new SimOnlyDto(25.0, ID,
        singletonList(SubscriptionDto.builder()
            .id(ID)
            .listPrice(45.0)
            .actualPrice(31.0)
            .duration(ContractDuration.ONE_YEAR)
            .name("KPN Zorgeloos Standaard")
            .slug("zorgeloos-standaard")
            .data(new DataBundleDto("10", BundleUnitType.GB))
            .voiceSmsNational(new VoiceSmsBundleDto("Onbeperkt", BundleUnitType.VOICE_SMS))
            .build()),
        completenessBenefits);
  }

  private MockMvc mvc;

  @Mock
  private SimOnlyService simOnlyService;

  @Before
  public void setUp() {
    initMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(new SimOnlyController(simOnlyService)).build();

  }

  @Test
  public void testGetSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(Completeness.DEFAULT,
        emptyList());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));

    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.NONE.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.DEFAULT.description())))
        .andExpect(jsonPath(BENEFITS, empty()));
    verify(simOnlyService).findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }

  @Test
  public void testPostNoneProductSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(Completeness.DEFAULT,
        emptyList());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));

    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.NONE.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.DEFAULT.description())))
        .andExpect(jsonPath(BENEFITS, empty()));
    verify(simOnlyService)
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }


  @Test
  public void testPostMobilePhoneProductSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(Completeness.MOMO,
        Completeness.MOMO.benefits());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));
    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.MOBILE_PHONE.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.MOMO.description())))
        .andExpect(jsonPath(BENEFITS, hasItems("MOBILE_DISCOUNT",
            "MOBILE_DISCOUNT",
            "FREE_CALLING",
            "DATA_BUNDLE_SHARING")));
    verify(simOnlyService)
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }

  @Test
  public void testPostInternetProductSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(Completeness.VAMO,
        Completeness.VAMO.benefits());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));
    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.INTERNET.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.VAMO.description())))
        .andExpect(jsonPath(BENEFITS,
            hasItems("MOBILE_DISCOUNT", "BUNDLE_MULTIPLIER")));
    verify(simOnlyService)
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }

  @Test
  public void testPostInternetAndTVProductSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(
        Completeness.VAMOTV,
        Completeness.VAMOTV.benefits());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));
    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.INTERNET.name())
            .param(PARAMETER, Product.INTERACTIVE_TV.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.VAMOTV.description())))
        .andExpect(jsonPath(BENEFITS,
            hasItems("MOBILE_DISCOUNT", "BUNDLE_MULTIPLIER", "FREE_TV_PACKAGE")));
    verify(simOnlyService)
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }

  @Test
  public void testPostInternetAndTVAndFixedPhoneProductSimOnly() throws Exception {
    final CompletenessBenefits completenessBenefits = new CompletenessBenefits(
        Completeness.VAMOTVLANDLINE,
        Completeness.VAMOTVLANDLINE.benefits());
    when(simOnlyService
        .findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class)))
        .thenReturn(createSimOnlyDto(completenessBenefits));

    final ResultActions resultActions = mvc.perform(
        get(SIMONLY_SUBSCRIPTIONS).param(PARAMETER, Product.INTERNET.name())
            .param(PARAMETER, Product.INTERACTIVE_TV.name())
            .param(PARAMETER, Product.FIXED_PHONE.name())
            .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    jsonAssertion(resultActions).andExpect(
        jsonPath(COMPLETENESS, is(Completeness.VAMOTVLANDLINE.description())))
        .andExpect(
            jsonPath(BENEFITS, hasItems("MOBILE_DISCOUNT", "BUNDLE_MULTIPLIER", "FREE_TV_PACKAGE",
                "FREE_CALLING")));

    verify(simOnlyService).findSimOnlySubscriptionsByFilter(any(CompletenessBenefits.class));
  }

  @Test
  public void testGetSubscriptionById404() throws Exception {
    mvc.perform(get("/simonly/subscription/id").contentType(APPLICATION_JSON)).andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetSubscriptionById200() throws Exception {
    final SimOnlyDto simOnlyDto = new SimOnlyDto();
    final String subIdToTest = "subId";
    SubscriptionDto subscriptionDto = new SubscriptionDto(subIdToTest, "subName", "subSlug", null,
        null, null, null, null, null);
    simOnlyDto.setSubscriptions(singletonList(subscriptionDto));

    when(simOnlyService.findSimOnlySubscriptionById(anyString()))
        .thenReturn(Optional.of(simOnlyDto));
    mvc.perform(get("/simonly/subscription/id").contentType(APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk());

    assertThat(simOnlyDto.getSubscriptions(),
        hasItem(
            hasProperty(SUBSCIPTION_ID, is(subIdToTest)))

    );
  }

  private ResultActions jsonAssertion(ResultActions resultActions) throws Exception {
    return resultActions.andExpect(
        jsonPath("$.subscriptions..list_price",
            hasItem(45.0)))
        .andExpect(
            jsonPath("$.subscriptions..id",
                hasItem(ID)));
  }
}

