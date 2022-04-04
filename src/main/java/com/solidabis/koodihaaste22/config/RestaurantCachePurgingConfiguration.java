package com.solidabis.koodihaaste22.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

import static com.solidabis.koodihaaste22.utils.Constants.CITY_CACHE_NAME;

@Configuration
@Slf4j
@EnableScheduling
public class RestaurantCachePurgingConfiguration {
    private final CacheManager cacheManager;

    public RestaurantCachePurgingConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    public void clearCaches() {
        log.info("Clearing city cache");
        var cache = cacheManager.getCache(CITY_CACHE_NAME);
        if(cache!=null) cache.clear();
    }
}
