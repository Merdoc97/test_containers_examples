package com.github.examples.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class Car {

  private String make;
  private String manufacturer;
  private UUID id;


  public Car() {

  }

  public Car(String make, String manufacturer, UUID id) {
    super();
    this.make = make;
    this.manufacturer = manufacturer;
    this.id = id;
  }

}
