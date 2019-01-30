package com.kpn.ecommerce.singleshop.cartservice.cart;

import com.kpn.ecommerce.singleshop.cartservice.client.ProductCatalogueClient;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class CartServiceTest {

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
  ProductCatalogueClient productCatalogueClient;

  @InjectMocks
  CartServiceImpl cartServiceImpl;

  @Before
  private void setup() {
//    when(productCatalogueClient.getSubscriptionById("f011f306-f7ef-11e8-8eb2-f2801f1b9fd1"))
//        .thenReturn();
  }

  @Test
  public void testGetOptionDataForCart() {

  }

}
