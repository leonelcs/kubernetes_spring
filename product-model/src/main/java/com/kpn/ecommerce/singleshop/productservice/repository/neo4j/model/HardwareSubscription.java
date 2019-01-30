package com.kpn.ecommerce.singleshop.productservice.repository.neo4j.model;

import java.time.LocalDateTime;

public class HardwareSubscription {

  private OrderType orderType;
  private LocalDateTime sacStartDate;
  private LocalDateTime sacEndDate;
  private ContractDuration contractDuration;
  private Integer amount;
  private Double sacDiscount;
  private Long id;
}
