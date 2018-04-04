package com.github.examples.config.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Car {

  private String make;
  private String manufacturer;
  private String id;

  public Car() {
    super();
  }

  public Car(String make, String manufacturer, String id) {
    super();
    this.make = make;
    this.manufacturer = manufacturer;
    this.id = id;
  }


  @Override
  public String toString() {
    return "Car [make=" + make + ", manufacturer=" + manufacturer + ", id=" + id + "]";
  }
}
