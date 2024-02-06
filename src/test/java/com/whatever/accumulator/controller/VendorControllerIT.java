package com.whatever.accumulator.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatever.accumulator.exception.KeyViolationException;
import com.whatever.accumulator.exception.ResourceNotFoundException;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.service.VendorService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VendorController.class)
class VendorControllerIT {

  private static final long ID = 1L;
  private static final String NAME = "Vendor";

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private VendorService vendorService;

  @Test
  void testAddVendor_success() throws Exception {
    Vendor vendor = new Vendor(ID, NAME);
    doReturn(vendor).when(vendorService).addVendor(vendor);

    String vendorStr = objectMapper.writeValueAsString(vendor);
    this.mvc
        .perform(post("/api/vendors").contentType(MediaType.APPLICATION_JSON).content(vendorStr))
        .andExpect(status().isCreated())
        .andExpect(content().string(equalTo(vendorStr)));
  }

  @Test
  void testAddVendor_existingId_error() throws Exception {
    Vendor vendor = new Vendor(ID, NAME);
    String errorMsg = "key violation";
    doThrow(new KeyViolationException(errorMsg)).when(vendorService).addVendor(vendor);

    String vendorStr = objectMapper.writeValueAsString(vendor);
    this.mvc
        .perform(post("/api/vendors").contentType(MediaType.APPLICATION_JSON).content(vendorStr))
        .andExpect(status().isConflict())
        .andExpect(content().string(equalTo(errorMsg)));
  }

  @Test
  void testRemoveVendor_success() throws Exception {
    doNothing().when(vendorService).removeVendor(ID);
    this.mvc.perform(delete("/api/vendors/" + ID)).andExpect(status().isOk());
  }

  @Test
  void testRemoveVendor_nonExistingId_statusNotFound() throws Exception {
    String errorMsg = "not found";
    doThrow(new ResourceNotFoundException(errorMsg)).when(vendorService).removeVendor(ID);
    this.mvc.perform(delete("/api/vendors/" + ID)).andExpect(status().isNotFound());
  }

  @Test
  void testGetVendor_success() throws Exception {
    Vendor vendor = new Vendor(ID, NAME);
    doReturn(vendor).when(vendorService).getVendor(ID);
    this.mvc
        .perform(get("/api/vendors/" + ID))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(vendor))));
  }

  @Test
  void testGetVendor_nonExistingId_statusNotFound() throws Exception {
    String errorMsg = "not found";
    doThrow(new ResourceNotFoundException(errorMsg)).when(vendorService).getVendor(ID);
    this.mvc.perform(get("/api/vendors/" + ID)).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllVendors_success() throws Exception {
    Vendor vendor = new Vendor(ID, NAME);
    List<Vendor> vendorList = List.of(vendor);
    doReturn(vendorList).when(vendorService).getAllVendors();
    this.mvc
        .perform(get("/api/vendors"))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(vendorList))));
  }

  @Test
  void testGetPricesByVendor_success() throws Exception {
    List<Price> priceList = List.of(new Price(1L, 1L, 1L, 100.0D, LocalDateTime.now()));
    doReturn(priceList).when(vendorService).getPricesByVendor(ID);

    this.mvc
        .perform(get("/api/vendors/" + ID + "/prices"))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(objectMapper.writeValueAsString(priceList))));
  }

  @Test
  void testGetPricesByVendor_nonExistingId_statusNotFound() throws Exception {
    doThrow(new ResourceNotFoundException("not found")).when(vendorService).getPricesByVendor(ID);

    this.mvc.perform(get("/api/vendors/" + ID + "/prices")).andExpect(status().isNotFound());
  }
}
