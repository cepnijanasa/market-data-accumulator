package com.whatever.accumulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.repository.PriceRepository;
import com.whatever.accumulator.repository.VendorRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendorServiceTest {

  private static final long ID = 1L;
  private static final String NAME = "VendorA";

  private VendorService vendorService;
  private VendorRepository vendorRepository;
  private PriceRepository priceRepository;

  @BeforeEach
  void setUp() {
    vendorRepository = mock(VendorRepository.class);
    priceRepository = mock(PriceRepository.class);
    vendorService = new VendorService(vendorRepository, priceRepository);
  }

  @Test
  void testAddVendor_success() {
    Vendor vendor = new Vendor(ID, NAME);
    when(vendorRepository.get(ID)).thenReturn(Optional.empty());
    when(vendorRepository.add(vendor)).thenReturn(vendor);
    Vendor added = vendorService.addVendor(vendor);
    assertThat(added).usingRecursiveComparison().isEqualTo(vendor);
  }

  @Test
  void testAddVendor_existingId_error() {
    Vendor vendor = new Vendor(ID, NAME);
    when(vendorRepository.get(ID)).thenReturn(Optional.of(vendor));
    assertThatThrownBy(() -> vendorService.addVendor(vendor))
        .isInstanceOf(KeyViolationException.class);
  }

  @Test
  void testRemoveVendor_nonExistingId_error() {
    when(vendorRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> vendorService.removeVendor(ID))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testRemoveVendor_success() {
    Vendor vendor = new Vendor(ID, NAME);
    when(vendorRepository.get(ID)).thenReturn(Optional.of(vendor));
    doNothing().when(vendorRepository).remove(ID);
    vendorService.removeVendor(ID);
    verify(vendorRepository).remove(ID);
  }

  @Test
  void testGetVendor_nonExistingId_error() {
    when(vendorRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> vendorService.getVendor(ID))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testGetVendor_success() {
    Vendor vendor = new Vendor(ID, NAME);
    when(vendorRepository.get(ID)).thenReturn(Optional.of(vendor));
    assertThat(vendorService.getVendor(ID)).usingRecursiveComparison().isEqualTo(vendor);
  }

  @Test
  void testGetAllVendors_success() {
    Vendor vendor = new Vendor(ID, NAME);
    List<Vendor> vendorList = List.of(vendor);
    doReturn(vendorList).when(vendorRepository).getAll();
    assertThat(vendorService.getAllVendors()).containsExactly(vendor);
  }

  @Test
  void testGetPricesByVendor_success() {
    Price price = new Price();
    List<Price> priceList = List.of(price);
    Vendor vendor = new Vendor(ID, NAME);
    doReturn(Optional.of(vendor)).when(vendorRepository).get(ID);
    doReturn(priceList).when(priceRepository).getPricesByVendor(ID);
    assertThat(vendorService.getPricesByVendor(ID)).containsExactly(price);
  }

  @Test
  void testGetPricesByVendor_nonExistingId_error() {
    when(vendorRepository.get(ID)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> vendorService.getPricesByVendor(ID))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
