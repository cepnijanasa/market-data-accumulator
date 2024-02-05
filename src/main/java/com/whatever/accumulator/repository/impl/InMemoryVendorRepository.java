package com.whatever.accumulator.repository.impl;

import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.repository.VendorRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryVendorRepository implements VendorRepository {

  private final Map<Long, Vendor> vendors;
  private final AtomicLong idGenerator = new AtomicLong();

  public InMemoryVendorRepository() {
    this.vendors = Collections.synchronizedMap(new HashMap<>());
  }

  @Override
  public Vendor add(Vendor vendor) {
    while (vendor.getId() == null) {
      Long id = idGenerator.incrementAndGet();
      if (get(id).isEmpty()) {
        vendor.setId(id);
      }
    }
    return vendors.put(vendor.getId(), vendor);
  }

  @Override
  public void remove(Long id) {
    vendors.remove(id);
  }

  @Override
  public Optional<Vendor> get(Long id) {
    return Optional.ofNullable(vendors.get(id));
  }

  @Override
  public Collection<Vendor> getAll() {
    return new ArrayList<>(vendors.values());
  }
}
