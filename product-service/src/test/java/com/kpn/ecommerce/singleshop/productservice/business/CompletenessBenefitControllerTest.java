package com.kpn.ecommerce.singleshop.productservice.business;

import static com.kpn.ecommerce.singleshop.productservice.business.CompletenessBenefitController.AVAILABLE_PRODUCTS;
import static com.kpn.ecommerce.singleshop.productservice.business.CompletenessBenefitController.COMPLETENESS;
import static com.kpn.ecommerce.singleshop.productservice.business.CompletenessBenefitController.COMPLETE_BENEFITS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Benefit;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class CompletenessBenefitControllerTest {

  private static final String PRODUCTS = "products";
  private static final String $_COMPLETENESS = "$.completeness";
  private static final String $_BENEFITS = "$.benefits";
  private static final String $ = "$";
  private MockMvc mvc;


  @Before
  public void setUp() {
    final CompletenessBenefitController objectUnderTest = new CompletenessBenefitController();
    mvc = MockMvcBuilders.standaloneSetup(objectUnderTest).build();
  }

  @Test
  public void testGetAvailableProducts() throws Exception {
    mvc.perform(get(AVAILABLE_PRODUCTS).contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($, containsInAnyOrder("NONE",
            "INTERNET",
            "INTERACTIVE_TV",
            "FIXED_PHONE",
            "MOBILE_PHONE")));

  }

  @Test
  public void testGetCompleteness() throws Exception {
    mvc.perform(get(COMPLETENESS).contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($, containsInAnyOrder("standaard",
            "vamo",
            "vamotvlandline",
            "vamotv",
            "momo")));
  }


  @Test
  public void testPostWithoutParametersCompleteBenefits() throws Exception {
    mvc.perform(post(COMPLETE_BENEFITS).contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($_COMPLETENESS, is(Completeness.DEFAULT.description())))
        .andExpect(jsonPath($_BENEFITS, empty()));

  }

  @Test
  public void testPostNoneProductCompleteBenefits() throws Exception {
    mvc.perform(
        post(COMPLETE_BENEFITS).param(PRODUCTS, Product.NONE.name()).contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($_COMPLETENESS, is(Completeness.DEFAULT.description())))
        .andExpect(jsonPath($_BENEFITS, empty()));
  }

  @Test
  public void testPostMobilePhoneProductCompleteBenefits() throws Exception {
    mvc.perform(
        post(COMPLETE_BENEFITS).param(PRODUCTS, Product.MOBILE_PHONE.name())
            .contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($_COMPLETENESS, is(Completeness.MOMO.description())))
        .andExpect(jsonPath($_BENEFITS, hasItems(Benefit.MOBILE_DISCOUNT.name(),
            Benefit.MOBILE_DISCOUNT.name(), Benefit.FREE_CALLING.name(),
            Benefit.DATA_BUNDLE_SHARING.name())));
  }

  @Test
  public void testPostInternetProductCompleteBenefits() throws Exception {
    mvc.perform(
        post(COMPLETE_BENEFITS).param(PRODUCTS, Product.INTERNET.name())
            .contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($_COMPLETENESS, is(Completeness.VAMO.description())))
        .andExpect(jsonPath($_BENEFITS, hasItems(Benefit.MOBILE_DISCOUNT.name(),
            Benefit.BUNDLE_MULTIPLIER.name())));
  }

  @Test
  public void testPostInternetAndTVProductCompleteBenefits() throws Exception {
    mvc.perform(
        post(COMPLETE_BENEFITS)
            .param(PRODUCTS, Product.INTERNET.name(), Product.INTERACTIVE_TV.name())
            .contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath($_COMPLETENESS, is(Completeness.VAMOTV.description())))
        .andExpect(jsonPath($_BENEFITS, hasItems(Benefit.MOBILE_DISCOUNT.name(),
            Benefit.BUNDLE_MULTIPLIER.name(),
            Benefit.FREE_TV_PACKAGE.name())));
  }

  @Test
  public void testPostInternetAndTVAndFixedPhoneProductCompleteBenefits() throws Exception {

    mvc.perform(
        post(COMPLETE_BENEFITS)
            .param(PRODUCTS, Product.INTERNET.name(), Product.INTERACTIVE_TV.name(),
                Product.FIXED_PHONE.name())
            .contentType(APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(
            jsonPath($_COMPLETENESS, is(Completeness.VAMOTVLANDLINE.description())))
        .andExpect(jsonPath($_BENEFITS, hasItems(Benefit.MOBILE_DISCOUNT.name(),
            Benefit.BUNDLE_MULTIPLIER.name(),
            Benefit.FREE_TV_PACKAGE.name(),
            Benefit.FREE_CALLING.name())));
  }

}