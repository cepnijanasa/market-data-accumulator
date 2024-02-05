package com.whatever.accumulator;

import com.whatever.accumulator.model.Instrument;
import com.whatever.accumulator.model.Price;
import com.whatever.accumulator.model.Vendor;
import com.whatever.accumulator.repository.InstrumentRepository;
import com.whatever.accumulator.repository.PriceRepository;
import com.whatever.accumulator.repository.VendorRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableScheduling
public class MarketDataAccumulatorApp {

  private final Random random = new Random();

  public static void main(String[] args) {
    SpringApplication.run(MarketDataAccumulatorApp.class, args);
  }

  @Bean
  public CommandLineRunner init(
      VendorRepository vendorRepository,
      InstrumentRepository instrumentRepository,
      PriceRepository priceRepository) {
    return args -> {
      List<String> vendorNames =
          List.of("Vendor A", "Vendor B", "Vendor C", "Vendor D", "Vendor E");
      for (String vendorName : vendorNames) {
        vendorRepository.add(new Vendor(vendorName));
      }

      List<String> instrumentNames =
          List.of("ABC", "XYZ", "CGR", "YPH", "OLQ", "MNO", "PQR", "DEF", "GHI", "JKL");
      for (String symbol : instrumentNames) {
        instrumentRepository.add(new Instrument(symbol, "USD"));
      }

      Flux.interval(Duration.ofSeconds(15))
          .map(
              val -> {
                Long vendorId = val % vendorNames.size();
                Long instrumentId = val % instrumentNames.size();
                Double value = random.nextDouble() * 100 + 100;
                LocalDateTime dateTimeCreated = LocalDateTime.now().minusDays(random.nextLong(40));

                Price price = new Price(vendorId, instrumentId, value, dateTimeCreated);
                priceRepository.add(price);
                return price;
              })
          .log()
          .subscribe();
    };
  }
}
