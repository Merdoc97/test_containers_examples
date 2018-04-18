package com.github.examples.config.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
public class Car {

  private String make;
  private String manufacturer;
  private UUID id;

  @JsonCreator
  public static Car create(String jsonString) throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    Car module = null;
    module = mapper.readValue(jsonString, Car.class);
    return module;
  }

  public Car() {

  }

  public Car(String make, String manufacturer, UUID id) {
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
