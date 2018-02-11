package com.github.examples.repository;


import com.github.examples.model.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by igor on 3/17/17.
 */
@Repository
public interface MenuEntityRepository extends JpaRepository<MenuEntity,Integer>{

    @Transactional(readOnly = true)
    @Query("select menu from MenuEntity menu join fetch menu.news news where news.isActive=true ")
    Set<MenuEntity> getActiveArticles();
}
