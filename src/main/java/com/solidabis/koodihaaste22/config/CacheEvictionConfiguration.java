package com.solidabis.koodihaaste22.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@EnableScheduling
public class CacheEvictionConfiguration {
    private final CacheManager cacheManager;

    public CacheEvictionConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    public void clearCaches() {
        log.info("Clearing city cache");
        var cache = cacheManager.getCache("cities");
        if(cache!=null) cache.clear();
    }
}
