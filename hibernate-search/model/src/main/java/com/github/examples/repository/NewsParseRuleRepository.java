package com.github.examples.repository;

import com.github.examples.model.NewsParseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by artyo on 26.02.2017.
 */
@Repository
public interface NewsParseRuleRepository extends JpaRepository<NewsParseRule,Integer>{

    @Query("select rule from NewsParseRule rule  " +
            "where rule.newsEntity.isActive=true")
    List<NewsParseRule>getActiveRules();
}
