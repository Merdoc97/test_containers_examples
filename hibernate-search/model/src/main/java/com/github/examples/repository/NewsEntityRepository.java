package com.github.examples.repository;

import com.github.examples.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by artyo on 26.02.2017.
 */
@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity,Integer>{
}
