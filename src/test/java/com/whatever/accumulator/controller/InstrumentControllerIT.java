package com.whatever.accumulator.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatever.accumulator.controller.InstrumentController;
import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.service.InstrumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InstrumentController.class)
class InstrumentControllerIT {

  public static final String SYMBOL = "ABC";
  public static final String CURRENCY = "GBP";
  public static final long ID = 1L;

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private InstrumentService instrumentService;

  @Test
  void testAddInstrument_success() throws Exception {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    doReturn(instrument).when(instrumentService).addInstrument(instrument);

    String instrumentStr = objectMapper.writeValueAsString(instrument);
    this.mvc
        .perform(
            post("/api/instruments").contentType(MediaType.APPLICATION_JSON).content(instrumentStr))
        .andExpect(status().isCreated())
        .andExpect(content().string(equalTo(instrumentStr)));
  }

  @Test
  void testAddInstrument_existingId_error() throws Exception {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    String errorMsg = "key violation";
    doThrow(new KeyViolationException(errorMsg)).when(instrumentService).addInstrument(instrument);

    String instrumentStr = objectMapper.writeValueAsString(instrument);
    this.mvc
        .perform(
            post("/api/instruments").contentType(MediaType.APPLICATION_JSON).content(instrumentStr))
        .andExpect(status().isConflict())
        .andExpect(content().string(equalTo(errorMsg)));
  }

  @Test
  void testRemoveInstrument_success() throws Exception {
    doNothing().when(instrumentService).removeInstrument(ID);
    this.mvc.perform(delete("/api/instruments/" + ID)).andExpect(status().isOk());
  }

  @Test
  void testRemoveInstrument_nonExistingId_statusNotFound() throws Exception {
    String errorMsg = "not found";
    doThrow(new ResourceNotFoundException(errorMsg)).when(instrumentService).removeInstrument(ID);
    this.mvc.perform(delete("/api/instruments/" + ID)).andExpect(status().isNotFound());
  }

  @Test
  void testGetInstrument_success() throws Exception {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    doReturn(instrument).when(instrumentService).getInstrument(ID);
    this.mvc
        .perform(get("/api/instruments/" + ID))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(instrument))));
  }

  @Test
  void testGetInstrument_nonExistingId_statusNotFound() throws Exception {
    String errorMsg = "not found";
    doThrow(new ResourceNotFoundException(errorMsg)).when(instrumentService).getInstrument(ID);
    this.mvc.perform(get("/api/instruments/" + ID)).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllInstruments_success() throws Exception {
    Instrument instrument = new Instrument(ID, SYMBOL, CURRENCY);
    List<Instrument> instrumentList = List.of(instrument);
    doReturn(instrumentList).when(instrumentService).getAllInstruments();
    this.mvc
        .perform(get("/api/instruments"))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(instrumentList))));
  }
}
