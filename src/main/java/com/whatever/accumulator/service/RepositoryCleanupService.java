package com.whatever.accumulator.service;

import com.whatever.accumulator.repository.PriceRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RepositoryCleanupService {
    private static final String CACHE_RETENTION_PROPERTY = "cache.retention-days";
    private static final String CACHE_CLEANUP_CRON_PROPERTY = "cache.cleanup.cron";
    
    private final PriceRepository priceRepository;

    @Value("${" + CACHE_RETENTION_PROPERTY + "}")
    private int cacheRetentionDays;

    public RepositoryCleanupService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Scheduled(cron = "${" + CACHE_CLEANUP_CRON_PROPERTY + ":-}")
    public void cleanup() {
        log.info("Running cleanup");
        LocalDateTime dateTime = LocalDateTime.now().minusDays(cacheRetentionDays);
        priceRepository.removePricesOlderThan(dateTime);
    }
}
