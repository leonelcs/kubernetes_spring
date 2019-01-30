package com.kpn.ecommerce.singleshop.productservice.simonly.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LineUpTest {

  @Test
  public void testLineUp() {
    assertThat(LineUp.KPN_MOBILE_2017.toString(), is("KPN Mobiel 2017"));
  }
}