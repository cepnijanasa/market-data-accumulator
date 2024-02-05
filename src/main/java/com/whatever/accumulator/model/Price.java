package com.whatever.accumulator.model;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Price {

  private Long id;
  private Long vendorId;
  private Long instrumentId;
  private Double value;
  private LocalDateTime dateTimeCreated;

  public Price(Long vendorId, Long instrumentId, Double value) {
    this.vendorId = vendorId;
    this.instrumentId = instrumentId;
    this.value = value;
    this.dateTimeCreated = LocalDateTime.now();
  }

  public Price(Long vendorId, Long instrumentId, Double value, LocalDateTime dateTimeCreated) {
    this.vendorId = vendorId;
    this.instrumentId = instrumentId;
    this.value = value;
    this.dateTimeCreated = dateTimeCreated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Price price = (Price) o;

    return Objects.equals(id, price.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
