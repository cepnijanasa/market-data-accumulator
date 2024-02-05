package com.whatever.accumulator.controller;

import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.service.InstrumentService;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

  private final InstrumentService instrumentService;

  public InstrumentController(InstrumentService instrumentService) {
    this.instrumentService = instrumentService;
  }

  @PostMapping
  public ResponseEntity<Instrument> addInstrument(@RequestBody Instrument instrument) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(instrumentService.addInstrument(instrument));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeInstrument(@PathVariable Long id) {
    instrumentService.removeInstrument(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
    return ResponseEntity.ok(instrumentService.getInstrument(id));
  }

  @GetMapping
  public ResponseEntity<Collection<Instrument>> getAllInstruments() {
    return ResponseEntity.ok(instrumentService.getAllInstruments());
  }

  @GetMapping("/{instrumentId}/prices")
  public ResponseEntity<Collection<Price>> getPricesByInstrument(@PathVariable Long instrumentId) {
    return ResponseEntity.ok(instrumentService.getPricesByInstrument(instrumentId));
  }
}
