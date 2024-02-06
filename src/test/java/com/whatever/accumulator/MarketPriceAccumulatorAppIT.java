package com.whatever.accumulator;

import com.whatever.accumulator.controller.InstrumentController;
import com.whatever.accumulator.controller.PriceController;
import com.whatever.accumulator.controller.VendorController;
import com.whatever.accumulator.repository.InstrumentRepository;
import com.whatever.accumulator.repository.PriceRepository;
import com.whatever.accumulator.repository.VendorRepository;
import com.whatever.accumulator.service.InstrumentService;
import com.whatever.accumulator.service.PriceService;
import com.whatever.accumulator.service.PriceRepositoryCleanupService;
import com.whatever.accumulator.service.VendorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MarketPriceAccumulatorAppIT {

	@Autowired
	private InstrumentRepository instrumentRepository;
	@Autowired
	private PriceRepository priceRepository;
	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private InstrumentService instrumentService;
	@Autowired
	private PriceService priceService;
	@Autowired
	private VendorService vendorService;

	@Autowired
	private InstrumentController instrumentController;
	@Autowired
	private PriceController priceController;
	@Autowired
	private VendorController vendorController;
	@Autowired
	private PriceRepositoryCleanupService cleanupService;

	@Test
	void contextLoads() {
		assertThat(instrumentRepository).isNotNull();
		assertThat(priceRepository).isNotNull();
		assertThat(vendorRepository).isNotNull();

		assertThat(instrumentService).isNotNull();
		assertThat(priceService).isNotNull();
		assertThat(vendorService).isNotNull();

		assertThat(instrumentController).isNotNull();
		assertThat(priceController).isNotNull();
		assertThat(vendorController).isNotNull();

		assertThat(cleanupService).isNotNull();
	}
}
