package com.github.examples.config.model;

import lombok.Getter;
import lombok.Setter;

/**
 */
@Getter
@Setter
public class People {
    private String name;
    private String secondName;

    public People() {
        super();
    }

    public People(String name, String secondName) {
        super();
        this.name = name;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
