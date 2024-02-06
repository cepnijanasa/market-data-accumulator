package com.whatever.accumulator.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.InstrumentRepository;
import com.whatever.accumulator.repository.PriceRepository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstrumentServiceTest {

  private static final String SYMBOL = "ABC";
  private static final String CURRENCY = "GBP";
  private static final long ID = 1L;
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
  void testAddInstrument_success() {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    when(instrumentRepository.get(ID)).thenReturn(Optional.empty());
    when(instrumentRepository.add(instrument)).thenReturn(instrument);
    Instrument added = instrumentService.addInstrument(instrument);
    assertThat(added).usingRecursiveComparison().isEqualTo(instrument);
  }

  @Test
  void testAddInstrument_existingId_error() {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    when(instrumentRepository.get(ID)).thenReturn(Optional.of(instrument));
    assertThatThrownBy(() -> instrumentService.addInstrument(instrument))
        .isInstanceOf(KeyViolationException.class);
  }

  @Test
  void testRemoveInstrument_nonExistingId_error() {
    when(instrumentRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> instrumentService.removeInstrument(ID))
            .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testRemoveInstrument_success() {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    when(instrumentRepository.get(ID)).thenReturn(Optional.of(instrument));
    doNothing().when(instrumentRepository).remove(ID);
    instrumentService.removeInstrument(ID);
    verify(instrumentRepository).remove(ID);
  }

  @Test
  void testGetInstrument_nonExistingId_error() {
    when(instrumentRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> instrumentService.getInstrument(ID))
            .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testGetInstrument_success() {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    when(instrumentRepository.get(ID)).thenReturn(Optional.of(instrument));
    assertThat(instrumentService.getInstrument(ID)).usingRecursiveComparison().isEqualTo(instrument);
  }

  @Test
  void testGetAllInstruments_success() {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    List<Instrument> instrumentList = List.of(instrument);
    doReturn(instrumentList).when(instrumentRepository).getAll();
    assertThat(instrumentService.getAllInstruments()).containsExactly(instrument);
  }

  @Test
  void testGetPricesByInstrument_success() {
    Price price = new Price();
    List<Price> priceList = List.of(price);
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    doReturn(Optional.of(instrument)).when(instrumentRepository).get(ID);
    doReturn(priceList).when(priceRepository).getPricesByInstrument(ID);
    assertThat(instrumentService.getPricesByInstrument(ID)).containsExactly(price);
  }

  @Test
  void testGetPricesByInstrument_nonExistingId_error() {
    when(instrumentRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> instrumentService.getPricesByInstrument(ID))
            .isInstanceOf(ResourceNotFoundException.class);
  }
}
