package com.github.examples.interfaces;


import com.github.examples.model.NewsBodyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface NewsSearchService {

    List<NewsBodyEntity>searchByMatchingField(String columnName, String searchValues, String columnSec, String colVal);
    Page<Object> searchByMatchingField(int pageNumber, int pageLength, String sortField, Sort.Direction sortOrder, Map<String, String> keyValue, String className) throws ClassNotFoundException;
}
