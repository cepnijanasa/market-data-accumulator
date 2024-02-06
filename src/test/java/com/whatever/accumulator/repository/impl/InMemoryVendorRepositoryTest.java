package com.whatever.accumulator.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.whatever.accumulator.model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryVendorRepositoryTest {

  public static final long ID = 1L;
  public static final String NAME = "VendorA";
  private InMemoryVendorRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryVendorRepository();
  }

  @Test
  void testAdd_withId_success() {
    Vendor vendor = new Vendor(ID, NAME);
    Vendor added = repository.add(vendor);
    assertThat(added).usingRecursiveComparison().isEqualTo(vendor);
  }

  @Test
  void testAdd_withoutId_success() {
    Vendor vendor = new Vendor(NAME);
    Vendor added = repository.add(vendor);
    assertThat(added)
        .returns(vendor.getName(), Vendor::getName)
        .extracting(Vendor::getId)
        .isNotNull();
  }

  @Test
  void testRemove_success() {
    Vendor vendor = new Vendor(ID, NAME);
    Vendor added = repository.add(vendor);
    assertThat(repository.get(added.getId())).isPresent();
    repository.remove(added.getId());
    assertThat(repository.get(added.getId())).isEmpty();
  }

  @Test
  void testGet_existingId_success() {
    assertThat(repository.get(ID)).isEmpty();
    Vendor vendor = new Vendor(ID, NAME);
    Vendor added = repository.add(vendor);
    assertThat(repository.get(added.getId())).isPresent();
  }

  @Test
  void testGet_nonExistingId_success() {
    assertThat(repository.get(ID)).isEmpty();
  }

  @Test
  void testGetAll_success() {
    Vendor vendor1 = new Vendor(1L, NAME);
    Vendor vendor2 = new Vendor(2L, NAME);
    repository.add(vendor1);
    repository.add(vendor2);
    assertThat(repository.getAll()).containsExactly(vendor1, vendor2);
  }
}
