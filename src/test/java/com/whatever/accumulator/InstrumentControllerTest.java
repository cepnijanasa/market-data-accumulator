package com.whatever.accumulator;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = InstrumentController.class)
class InstrumentControllerIT {

  public static final String SYMBOL = "ABC";
  public static final String CURRENCY = "GBP";
  public static final long ID = 1L;

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private InstrumentService instrumentService;

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
}
