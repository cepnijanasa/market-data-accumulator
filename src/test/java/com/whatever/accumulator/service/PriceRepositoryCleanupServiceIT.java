package com.whatever.accumulator.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.PriceRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceRepositoryCleanupServiceIT {

  private static final long ID = 1L;
  private static final long VENDOR_ID = 1L;
  private static final long INSTRUMENT_ID = 1L;
  private static final double VALUE = 123.20D;

  @Autowired
  private PriceRepositoryCleanupService cleanupService;
  @Autowired
  private PriceRepository priceRepository;

  @Test
  void testCleanup() {
    LocalDateTime tooOldToStay =
        LocalDateTime.now().minusDays(cleanupService.getCacheRetentionDays() + 1);
    Price price = new Price(ID, VENDOR_ID, INSTRUMENT_ID, VALUE, tooOldToStay);
    priceRepository.add(price);
    assertThat(priceRepository.getAll()).isNotEmpty();
    cleanupService.cleanup();
    assertThat(priceRepository.getAll()).isEmpty();
  }
}
