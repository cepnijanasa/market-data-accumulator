package com.whatever.accumulator.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.service.PriceService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PriceController.class)
class PriceControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private PriceService priceService;

  @Test
  void testAddInstrument_success() throws Exception {
    Price price = new Price(1L, 1L, 1L, 123.20D, LocalDateTime.now());
    doReturn(price).when(priceService).addPrice(price);

    String priceStr = objectMapper.writeValueAsString(price);
    this.mvc
        .perform(post("/api/prices").contentType(MediaType.APPLICATION_JSON).content(priceStr))
        .andExpect(status().isCreated())
        .andExpect(content().string(equalTo(priceStr)));
  }

  @Test
  void testAddPrice_existingId_error() throws Exception {
    Price price = new Price(1L, 1L, 1L, 123.20D, LocalDateTime.now());
    String errorMsg = "key violation";
    doThrow(new KeyViolationException(errorMsg)).when(priceService).addPrice(price);

    String priceStr = objectMapper.writeValueAsString(price);
    this.mvc
        .perform(post("/api/prices").contentType(MediaType.APPLICATION_JSON).content(priceStr))
        .andExpect(status().isConflict())
        .andExpect(content().string(equalTo(errorMsg)));
  }
}
