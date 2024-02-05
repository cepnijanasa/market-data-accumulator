package com.whatever.accumulator.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.whatever.accumulator.model.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryInstrumentRepositoryTest {

  private static final String CURRENCY = "GBP";
  private static final String SYMBOL = "ABC";

  private InMemoryInstrumentRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryInstrumentRepository();
  }

  @Test
  void testAdd_withId_success() {
    Instrument instrument = new Instrument(1L, SYMBOL, CURRENCY);
    Instrument added = repository.add(instrument);
    assertThat(added).usingRecursiveComparison().isEqualTo(instrument);
  }

  @Test
  void testAdd_withoutId_success() {
    Instrument instrument = new Instrument(SYMBOL, CURRENCY);
    Instrument added = repository.add(instrument);
    assertThat(added)
        .returns(instrument.getSymbol(), Instrument::getSymbol)
        .returns(instrument.getCurrency(), Instrument::getCurrency)
        .extracting(Instrument::getId)
        .isNotNull();
  }

  @Test
  void testRemove_success() {
    Instrument instrument = new Instrument(1L, SYMBOL, CURRENCY);
    Instrument added = repository.add(instrument);
    assertThat(repository.get(added.getId())).isPresent();
    repository.remove(added.getId());
    assertThat(repository.get(added.getId())).isEmpty();
  }

  @Test
  void testGet_existingId_success() {
    Long id = 1L;
    assertThat(repository.get(id)).isEmpty();
    Instrument instrument = new Instrument(id, SYMBOL, CURRENCY);
    Instrument added = repository.add(instrument);
    assertThat(repository.get(added.getId())).isPresent();
  }

  @Test
  void testGet_nonExistingId_success() {
    assertThat(repository.get(1L)).isEmpty();
  }

  @Test
  void testGetAll_success() {
    repository.add(new Instrument(1L, SYMBOL, CURRENCY));
    repository.add(new Instrument(2L, SYMBOL, CURRENCY));
    assertThat(repository.getAll()).size().isEqualTo(2);
  }

  @Test
  void testGetAll_empty_success() {
    assertThat(repository.getAll()).size().isEqualTo(0);
  }
}
