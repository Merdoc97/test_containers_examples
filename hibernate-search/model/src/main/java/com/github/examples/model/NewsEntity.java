package com.github.examples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

/**
 * Created by artyo on 26.02.2017.
 */
@Entity
@Indexed
@Table(name = "news")
@Data
public class NewsEntity {
    @Id
    @Column(name = "news_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_topic")
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.YES)
    @SortableField
    private String articleTopic;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_parsed_today")
    private Boolean isParsedToday;

    @Column(name = "top_name_fk")
    private Integer topFk;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "top_name_fk",referencedColumnName = "tpname_pk",insertable = false,updatable = false)
    private MenuEntity menuEntity;
}
