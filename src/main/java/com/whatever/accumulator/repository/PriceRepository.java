package com.whatever.accumulator.repository;

import com.whatever.accumulator.model.Price;

import java.time.LocalDateTime;
import java.util.Collection;

public interface PriceRepository extends CommonRepository<Price, Long> {

  Collection<Price> getPricesByVendor(Long vendorId);

  Collection<Price> getPricesByInstrument(Long instrumentId);

  void removePricesOlderThan(LocalDateTime dateTime);
}
