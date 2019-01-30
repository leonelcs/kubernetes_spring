package com.kpn.ecommerce.singleshop.productservice.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import com.google.common.collect.Lists;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

  private static final String CACHE_SUBSCRIPTIONS = "simonly/subscriptions";

  @Bean
  public CacheManager cacheManager(Ticker ticker) {
    CaffeineCache subscriptionsCache = buildCache(CACHE_SUBSCRIPTIONS, ticker, 60);

    SimpleCacheManager manager = new SimpleCacheManager();
    manager.setCaches(Lists.newArrayList(subscriptionsCache));
    return manager;
  }

  @Bean
  public Ticker ticker() {
    return Ticker.systemTicker();
  }

  private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
    return new CaffeineCache(name,
        Caffeine.newBuilder().expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .maximumSize(100).ticker(ticker).build());
  }

}
