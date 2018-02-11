package com.github.examples.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by artyo on 26.02.2017.
 */
@Entity
@Indexed
@Table(name = "news_body")
@Data
public class NewsBodyEntity {
    @Id
    @Column(name = "article_link")

    private String articleLink;

    @Column(name = "article_name")
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.YES)
    @SortableField
    private String articleName;

    @Column(name = "article_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.YES)
    @SortableField
    private LocalDate articleDate;

    @Column(name = "article_body")
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.YES)
    @SortableField
    private String articleBody;

    @Column(name = "news_fk")
    private Integer newsFk;

    @ManyToOne
    @JoinColumn(name = "news_fk",referencedColumnName = "news_pk",insertable = false,updatable = false)
    @IndexedEmbedded
    private NewsEntity newsEntity;

}
