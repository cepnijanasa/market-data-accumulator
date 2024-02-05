package com.whatever.accumulator.controller;

import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.service.VendorService;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

  private final VendorService vendorService;

  public VendorController(VendorService vendorService) {
    this.vendorService = vendorService;
  }

  @PostMapping
  public ResponseEntity<Vendor> addVendor(@RequestBody Vendor vendor) {
    return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.addVendor(vendor));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeVendor(@PathVariable Long id) {
    vendorService.removeVendor(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Vendor> getVendor(@PathVariable Long id) {
    return ResponseEntity.ok(vendorService.getVendor(id));
  }

  @GetMapping
  public ResponseEntity<Collection<Vendor>> getAllVendors() {
    return ResponseEntity.ok(vendorService.getAllVendors());
  }

  @GetMapping("/{vendorId}/prices")
  public ResponseEntity<Collection<Price>> getPricesByVendor(@PathVariable Long vendorId) {
    return ResponseEntity.ok(vendorService.getPricesByVendor(vendorId));
  }
}
