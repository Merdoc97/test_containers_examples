package com.github.examples.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by igor on 3/17/17.
 */

@Entity
@Table(name = "top_name")
@Data
public class MenuEntity {

    @Id
    @Column(name = "tpname_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",nullable = false,unique = true)
    private String menuName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "menuEntity")
    private List<NewsEntity>news;



}
