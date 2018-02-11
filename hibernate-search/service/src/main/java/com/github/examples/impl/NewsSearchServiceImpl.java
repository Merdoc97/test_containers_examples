package com.github.examples.impl;


import com.github.examples.interfaces.NewsSearchService;
import com.github.examples.model.NewsBodyEntity;
import lombok.AllArgsConstructor;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by igor on 2/7/18.
 */
@AllArgsConstructor
@Transactional(readOnly = true)
public class NewsSearchServiceImpl implements NewsSearchService {

    private final FullTextEntityManager manager;

    @Override
    public List<NewsBodyEntity> searchByMatchingField(String columnName, String searchValues,
                                                      String columnName2, String searchValues2) {

        final QueryBuilder queryBuilder = manager.getSearchFactory().buildQueryBuilder().forEntity(NewsBodyEntity.class).get();

        Query query = queryBuilder.bool()
                .must(queryBuilder.phrase().onField(columnName).sentence(searchValues).createQuery())
                .must(queryBuilder.phrase().onField(columnName2).sentence(searchValues2).createQuery()).createQuery();

        FullTextQuery fullTextQuery = manager.createFullTextQuery(query, NewsBodyEntity.class);

        return fullTextQuery.getResultList();


    }

    @Override
    public Page<Object> searchByMatchingField(int pageNumber, int pageLength, String sortField, Sort.Direction sortOrder, Map<String, String> keyValue, String className) throws ClassNotFoundException {

        final QueryBuilder queryBuilder = manager.getSearchFactory().buildQueryBuilder().forEntity(Class.forName(className)).get();

        BooleanJunction<BooleanJunction> booleanJunction = queryBuilder.bool();
        keyValue.forEach((columnName, columnValue) -> booleanJunction
                .must(queryBuilder.phrase().onField(columnName).sentence(columnValue)
                        .createQuery()));

        FullTextQuery fullTextQuery = manager.createFullTextQuery(booleanJunction.createQuery(), Class.forName(className));

        if (Objects.nonNull(sortField) && Objects.nonNull(sortOrder)) {
            if (sortOrder.equals(Sort.Direction.ASC)) {
                org.apache.lucene.search.Sort sortAsc = queryBuilder.sort()
                        .byField(sortField)
                        .asc()
                        .createSort();
                fullTextQuery.setSort(sortAsc);
            }
            if (sortOrder.equals(Sort.Direction.DESC)) {
                org.apache.lucene.search.Sort sortDesc = queryBuilder.sort()
                        .byField(sortField)
                        .desc()
                        .createSort();
                fullTextQuery.setSort(sortDesc);
            }
        }
        int total = fullTextQuery.getResultSize();
        fullTextQuery.setFirstResult(pageNumber * pageLength);
        fullTextQuery.setMaxResults(pageLength);
        return new PageImpl<>(fullTextQuery.getResultList(), new PageRequest(pageNumber, pageLength), total);
    }
}
