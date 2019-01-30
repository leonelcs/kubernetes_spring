package com.kpn.ecommerce.singleshop.productimporter.query;

import java.util.Map;

public interface QueryRunner {

  void execute(String query);

  void execute(String query, Map<String, Object> params);
}
