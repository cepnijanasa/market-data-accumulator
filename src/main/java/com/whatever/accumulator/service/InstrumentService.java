package com.whatever.accumulator.service;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.InstrumentRepository;
import com.whatever.accumulator.repository.PriceRepository;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class InstrumentService {

  private static final String MSG_NOT_FOUND_TEMPLATE = "Instrument with id=%s not found";
  private static final String MSG_KEY_VIOLATION_TEMPLATE = "Instrument with id=%s already exists";

  private final InstrumentRepository instrumentRepository;
  private final PriceRepository priceRepository;

  public InstrumentService(
      InstrumentRepository instrumentRepository, PriceRepository priceRepository) {
    this.instrumentRepository = instrumentRepository;
    this.priceRepository = priceRepository;
  }

  public Instrument addInstrument(Instrument instrument) {
    if (instrument.getId() != null && instrumentRepository.get(instrument.getId()).isPresent()) {
      // ID already exists
      throw new KeyViolationException(String.format(MSG_KEY_VIOLATION_TEMPLATE, instrument.getId()));
    }
    return instrumentRepository.add(instrument);
  }

  public void removeInstrument(Long id) {
    Optional<Instrument> instrument = instrumentRepository.get(id);
    if (instrument.isPresent()) {
      instrumentRepository.remove(id);
    } else {
      throw new ResourceNotFoundException(String.format(MSG_NOT_FOUND_TEMPLATE, id));
    }
  }

  public Instrument getInstrument(Long id) {
    Optional<Instrument> instrument = instrumentRepository.get(id);
    return instrument.orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_NOT_FOUND_TEMPLATE, id)));
  }

  public Collection<Instrument> getAllInstruments() {
    return instrumentRepository.getAll();
  }

  public Collection<Price> getPricesByInstrument(Long instrumentId) {
    return priceRepository.getPricesByInstrument(instrumentId);
  }
}
