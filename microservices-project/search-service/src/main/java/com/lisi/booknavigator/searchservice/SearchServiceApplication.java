package com.lisi.booknavigator.searchservice;

import com.lisi.booknavigator.searchservice.service.ElasticsearchService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;



@SpringBootApplication
public class SearchServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(SearchServiceApplication.class);

    public static void main(String[] args) {
        //SpringApplication.run(SearchServiceApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(SearchServiceApplication.class, args);

        // Retrieve the ElasticsearchService bean
        ElasticsearchService elasticsearchService = context.getBean(ElasticsearchService.class);

        // Reload all properties to Elasticsearch
        elasticsearchService.reloadProperties();
    }
}
