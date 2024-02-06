package com.whatever.accumulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceServiceTest {
  private static final long ID = 1L;
  private static final long VENDOR_ID = 1L;
  private static final long INSTRUMENT_ID = 1L;
  private static final double VALUE = 123.20D;
  private static final LocalDateTime DATE_TIME_CREATED = LocalDateTime.now();

  private PriceService priceService;
  private PriceRepository priceRepository;

  @BeforeEach
  void setUp() {
    priceRepository = mock(PriceRepository.class);
    priceService = new PriceService(priceRepository);
  }

  @Test
  void testAddPrice_success() {
    Price price = new Price(ID, VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    when(priceRepository.get(ID)).thenReturn(Optional.empty());
    when(priceRepository.add(price)).thenReturn(price);
    Price added = priceService.addPrice(price);
    assertThat(added).usingRecursiveComparison().isEqualTo(price);
  }

  @Test
  void testAddPrice_existingId_error() {
    Price price = new Price(ID, VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    when(priceRepository.get(ID)).thenReturn(Optional.of(price));
    assertThatThrownBy(() -> priceService.addPrice(price))
        .isInstanceOf(KeyViolationException.class);
  }
}
