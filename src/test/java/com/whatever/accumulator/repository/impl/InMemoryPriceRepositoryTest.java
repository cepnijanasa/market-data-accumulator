package com.whatever.accumulator.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.whatever.accumulator.model.Price;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryPriceRepositoryTest {

  private static final long ID = 1L;
  private static final long VENDOR_ID = 1L;
  private static final long INSTRUMENT_ID = 1L;
  private static final double VALUE = 123.20D;
  private static final LocalDateTime DATE_TIME_CREATED = LocalDateTime.now();
  private InMemoryPriceRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryPriceRepository();
  }

  @Test
  void testAdd_withId_success() {
    Price price = new Price(ID, VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    Price added = repository.add(price);
    assertThat(added).usingRecursiveComparison().isEqualTo(price);
  }

  @Test
  void testAdd_withoutId_success() {
    Price price = new Price(VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    Price added = repository.add(price);
    assertThat(added)
        .returns(price.getVendorId(), Price::getVendorId)
        .returns(price.getInstrumentId(), Price::getInstrumentId)
        .returns(price.getDateTimeCreated(), Price::getDateTimeCreated)
        .extracting(Price::getId)
        .isNotNull();
  }

  @Test
  void testRemove_success() {
    Price price = new Price(VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    Price added = repository.add(price);
    assertThat(repository.get(added.getId())).isPresent();
    repository.remove(added.getId());
    assertThat(repository.get(added.getId())).isEmpty();
  }

  @Test
  void testGet_existingId_success() {
    assertThat(repository.get(ID)).isEmpty();
    Price price = new Price(VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    Price added = repository.add(price);
    assertThat(repository.get(added.getId())).isPresent();
  }

  @Test
  void testGet_nonExistingId_success() {
    assertThat(repository.get(ID)).isEmpty();
  }

  @Test
  void testGetAll_success() {
    Price price1 = new Price(1L, VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    Price price2 = new Price(2L, VENDOR_ID, INSTRUMENT_ID, VALUE, DATE_TIME_CREATED);
    repository.add(price1);
    repository.add(price2);
    assertThat(repository.getAll()).containsExactly(price1, price2);
  }
}
