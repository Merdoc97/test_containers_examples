package com.github.examples.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class Vacancy
{
    private String title;
    private String salary;
    private String city;
    private String companyName;
    private String siteName;
    private String url;

}
