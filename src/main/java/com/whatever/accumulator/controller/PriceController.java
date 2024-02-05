package com.whatever.accumulator.controller;

import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.service.PriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

  private final PriceService priceService;

  public PriceController(PriceService priceService) {
    this.priceService = priceService;
  }

  @PostMapping
  public ResponseEntity<Price> addPrice(@RequestBody Price price) {
    return ResponseEntity.status(HttpStatus.CREATED).body(priceService.addPrice(price));
  }
}
