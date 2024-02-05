package com.whatever.accumulator.service;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.PriceRepository;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

  private final PriceRepository priceRepository;

  public PriceService(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  private static final String MSG_KEY_VIOLATION_TEMPLATE = "Price with id=%s already exists";

  public Price addPrice(Price price) {
    if (price.getId() != null && priceRepository.get(price.getId()).isEmpty()) {
      // ID already exists
      throw new KeyViolationException(String.format(MSG_KEY_VIOLATION_TEMPLATE, price.getId()));
    }
    return priceRepository.add(price);
  }
}
