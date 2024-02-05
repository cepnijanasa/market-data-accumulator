package com.whatever.accumulator.repository.impl;

import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.repository.InstrumentRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryInstrumentRepository implements InstrumentRepository {

  private final Map<Long, Instrument> instruments;
  private final AtomicLong idGenerator = new AtomicLong();

  public InMemoryInstrumentRepository() {
    this.instruments = Collections.synchronizedMap(new HashMap<>());
  }

  @Override
  public Instrument add(Instrument instrument) {
    if (instrument.getId() == null) {
      instrument.setId(idGenerator.incrementAndGet());
    }
    instruments.put(instrument.getId(), instrument);
    return instrument;
  }

  @Override
  public void remove(Long id) {
    instruments.remove(id);
  }

  @Override
  public Optional<Instrument> get(Long id) {
    return Optional.ofNullable(instruments.get(id));
  }

  @Override
  public Collection<Instrument> getAll() {
    return new ArrayList<>(instruments.values());
  }
}
