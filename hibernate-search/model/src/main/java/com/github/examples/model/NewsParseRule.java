package com.github.examples.model;

import lombok.Data;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;

/**
 * Created by artyo on 26.02.2017.
 */
@Entity
@Indexed
@Table(name = "news_parse_rule")
@Data
public class NewsParseRule {
    @Id
    @Column(name = "rule_pk")
    private Integer id;

    @Column(name = "url_format")
    @Field(store = Store.YES)
    private String urlFormat;

    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "element_class")
    private String elementsClass;

    @Column(name = "child_article_name")
    private String articleName;

    @Column(name = "child_lk_rd_more")
    private String linkReadMore;

    @Column(name = "child_article_body")
    private String articleBody;

    @Column(name = "news_bd_fk")
    private Integer newsId;

    @Column(name = "maxpagenumbers")
    private Integer maxPages;

    @Column(name = "isonepage")
    private boolean isOnePage;

    @OneToOne
    @JoinColumn(name = "news_bd_fk", referencedColumnName = "news_pk", insertable = false, updatable = false)
    private NewsEntity newsEntity;
}
