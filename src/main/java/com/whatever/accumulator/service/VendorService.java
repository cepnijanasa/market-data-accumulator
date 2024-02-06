package com.whatever.accumulator.service;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.repository.PriceRepository;
import com.whatever.accumulator.repository.VendorRepository;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class VendorService {

  private static final String MSG_NOT_FOUND_TEMPLATE = "Vendor with id=%s not found";
  private static final String MSG_KEY_VIOLATION_TEMPLATE = "Vendor with id=%s already exists";

  private final VendorRepository vendorRepository;
  private final PriceRepository priceRepository;

  public VendorService(VendorRepository vendorRepository, PriceRepository priceRepository) {
    this.vendorRepository = vendorRepository;
    this.priceRepository = priceRepository;
  }

  public Vendor addVendor(Vendor vendor) {
    if (vendor.getId() != null && vendorRepository.get(vendor.getId()).isPresent()) {
      // ID already exists
      throw new KeyViolationException(String.format(MSG_KEY_VIOLATION_TEMPLATE, vendor.getId()));
    }
    return vendorRepository.add(vendor);
  }

  public void removeVendor(Long id) {
    Optional<Vendor> vendor = vendorRepository.get(id);
    if (vendor.isPresent()) {
      vendorRepository.remove(id);
    } else {
      throw new ResourceNotFoundException(String.format(MSG_NOT_FOUND_TEMPLATE, id));
    }
  }

  public Vendor getVendor(Long id) {
    Optional<Vendor> vendor = vendorRepository.get(id);
    return vendor.orElseThrow(
        () -> new ResourceNotFoundException(String.format(MSG_NOT_FOUND_TEMPLATE, id)));
  }

  public Collection<Vendor> getAllVendors() {
    return vendorRepository.getAll();
  }

  public Collection<Price> getPricesByVendor(Long vendorId) {
    Vendor vendor =
        vendorRepository
            .get(vendorId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(String.format(MSG_NOT_FOUND_TEMPLATE, vendorId)));
    return priceRepository.getPricesByVendor(vendorId);
  }
}
