package com.whatever.accumulator.model;

import java.util.Objects;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Vendor {

  private Long id;
  private String name;

  public Vendor(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vendor vendor = (Vendor) o;

      return Objects.equals(id, vendor.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
