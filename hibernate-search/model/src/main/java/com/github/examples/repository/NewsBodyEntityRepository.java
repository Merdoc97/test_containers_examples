package com.github.examples.repository;


import com.github.examples.model.NewsBodyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by artyo on 26.02.2017.
 */
@Repository
public interface NewsBodyEntityRepository extends JpaRepository<NewsBodyEntity,String> {

    @Transactional(readOnly = true)
    @Query("select news from NewsBodyEntity news where news.newsFk=" +
            "(select newsEntity.id from NewsEntity newsEntity where newsEntity.articleTopic=:articleTopic) " +
            "order by news.articleDate desc")
    Page<NewsBodyEntity> getNewsByTopicId(@Param("articleTopic") String articleTopic, Pageable pageable);
}
