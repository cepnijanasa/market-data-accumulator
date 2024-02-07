package com.whatever.accumulator.repository.impl;

import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * I understand that having 3-levels-deep map adds a lot of complexity and negatively impacts maintainability.
 * But I wanted to have fun here. I imagined huge volumes of data, and I've put a lot of emphasis on the fact
 * that clients would often be pulling all prices for a particular vendor or prices for a single instrument
 * from various vendors. My motivation was to avoid filtering which would be necessary
 * if we had all the prices in a single map. It would be interesting to performance-test
 * these two different approaches with varying volumes of data.
 */

@Repository
@Slf4j
public class InMemoryPriceRepository implements PriceRepository {

  private final Map<Long, Map<Long, Map<Long, Price>>> pricesByVendor;
  private final AtomicLong idGenerator = new AtomicLong();

  public InMemoryPriceRepository() {
    this.pricesByVendor = Collections.synchronizedMap(new HashMap<>());
  }

  @Override
  public Price add(Price price) {
    if (price.getId() == null) {
      price.setId(idGenerator.incrementAndGet());
    }
    Map<Long, Map<Long, Price>> pricesByInstrument =
        pricesByVendor.computeIfAbsent(
            price.getVendorId(), key -> Collections.synchronizedMap(new HashMap<>()));

    Map<Long, Price> prices =
        pricesByInstrument.computeIfAbsent(
            price.getInstrumentId(), key -> Collections.synchronizedMap(new TreeMap<>()));

    prices.put(price.getId(), price);
    return prices.get(price.getId());
  }

  @Override
  public void remove(Long id) {
    for (Map<Long, Map<Long, Price>> pricesByInstrument : pricesByVendor.values()) {
      for (Map<Long, Price> prices : pricesByInstrument.values()) {
        Price removed = prices.remove(id);
        if (removed != null) {
          return;
        }
      }
    }
  }

  @Override
  public Optional<Price> get(Long id) {
    for (Map<Long, Map<Long, Price>> pricesByInstrument : pricesByVendor.values()) {
      for (Map<Long, Price> prices : pricesByInstrument.values()) {
        Price found = prices.get(id);
        if (found != null) {
          return Optional.of(found);
        }
      }
    }
    return Optional.empty();
  }

  @Override
  public Collection<Price> getAll() {
    List<Price> allPrices = new ArrayList<>();
    for (Map<Long, Map<Long, Price>> pricesByInstrument : pricesByVendor.values()) {
      for (Map<Long, Price> prices : pricesByInstrument.values()) {
        allPrices.addAll(prices.values());
      }
    }
    return allPrices;
  }

  @Override
  public Collection<Price> getPricesByVendor(Long vendorId) {
    List<Price> allPricesByVendor = new ArrayList<>();
    Map<Long, Map<Long, Price>> pricesByInstrument = pricesByVendor.get(vendorId);
    if (pricesByInstrument != null) {
      for (Map<Long, Price> prices : pricesByInstrument.values()) {
        allPricesByVendor.addAll(prices.values());
      }
    }
    return allPricesByVendor;
  }

  @Override
  public Collection<Price> getPricesByInstrument(Long instrumentId) {
    List<Price> allPricesByInstrument = new ArrayList<>();
    for (Map<Long, Map<Long, Price>> pricesByInstrument : pricesByVendor.values()) {
      Map<Long, Price> prices = pricesByInstrument.get(instrumentId);
      if (prices != null) {
        allPricesByInstrument.addAll(prices.values());
      }
    }
    return allPricesByInstrument;
  }

  @Override
  public void removePricesOlderThan(LocalDateTime dateTime) {
    for (Map<Long, Map<Long, Price>> pricesByInstrument : pricesByVendor.values()) {
      for (Map<Long, Price> prices : pricesByInstrument.values()) {
        for (Price price : prices.values()) {
          if (price.getDateTimeCreated().isBefore(dateTime)) {
            log.info("Removing " + price);
            prices.remove(price.getId());
          }
        }
      }
    }
  }
}
