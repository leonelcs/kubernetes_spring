package com.kpn.ecommerce.singleshop.productservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.kpn.ecommerce.singleshop.productservice.dto.ContractDuration;
import org.junit.Test;

public class ContractDurationTest {


  @Test(expected = IllegalArgumentException.class)
  public void fromValueIllegalArgument() {
    ContractDuration.fromValue(1);
  }

  @Test
  public void fromValueLegalArgument() {
    assertThat(ContractDuration.fromValue(12), is(ContractDuration.ONE_YEAR));
    assertThat(ContractDuration.fromValue(24), is(ContractDuration.TWO_YEAR));
  }

  @Test
  public void getMonths() {
    assertThat(ContractDuration.ONE_YEAR.getMonths(), is(12));
    assertThat(ContractDuration.TWO_YEAR.getMonths(), is(24));
  }

  @Test
  public void testToString() {
    assertThat(ContractDuration.ONE_YEAR.toString(), is("1-jaar"));
    assertThat(ContractDuration.TWO_YEAR.toString(), is("2-jaar"));
  }

}