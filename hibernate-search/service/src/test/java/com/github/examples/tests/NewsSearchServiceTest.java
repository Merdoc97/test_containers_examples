package com.github.examples.tests;

import com.github.examples.config.TestConfiguration;
import com.github.examples.interfaces.NewsSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class NewsSearchServiceTest extends TestConfiguration{


    @Autowired
    @Qualifier("newsSearchService")
    private NewsSearchService searchService;


    @Test
    public void testElasticEmbedded() throws ClassNotFoundException {
        Map<String,String>searchValues=new HashMap<>();
        searchValues.put("urlFormat","www");
        Page result=searchService.searchByMatchingField(0,10,null,null,searchValues, "com.github.examples.model.NewsParseRule");
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getContent().size());
    }
}
