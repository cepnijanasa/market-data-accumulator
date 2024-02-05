package com.whatever.accumulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.repository.InstrumentRepository;
import com.whatever.accumulator.repository.PriceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstrumentServiceTest {

  private InstrumentService instrumentService;
  private InstrumentRepository instrumentRepository;
  private PriceRepository priceRepository;

  @BeforeEach
  void setUp() {
    instrumentRepository = mock(InstrumentRepository.class);
    priceRepository = mock(PriceRepository.class);
    instrumentService = new InstrumentService(instrumentRepository, priceRepository);
  }

  @Test
  void testAddInstrument_nonExistingId_success() {
    Instrument instrument = new Instrument(1L, "ABC", "GBP");
    when(instrumentRepository.get(any(Long.class))).thenReturn(Optional.empty());
    when(instrumentRepository.add(any(Instrument.class))).thenReturn(instrument);
    Instrument added = instrumentService.addInstrument(instrument);
    assertThat(added).usingRecursiveComparison().isEqualTo(instrument);
  }

  @Test
  void testAddInstrument_existingId_error() {
    Instrument instrument = new Instrument(1L, "ABC", "GBP");
    when(instrumentRepository.get(any(Long.class))).thenReturn(Optional.of(instrument));
    assertThatThrownBy(() -> instrumentService.addInstrument(instrument))
        .isInstanceOf(KeyViolationException.class);
  }
}
