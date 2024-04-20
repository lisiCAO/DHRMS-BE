package com.lisi.booknavigator.searchservice;

import com.lisi.booknavigator.searchservice.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Slf4j
@SpringBootApplication
public class SearchServiceApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(SearchServiceApplication.class, args);

        // Retrieve the ElasticsearchService bean
        ElasticsearchService elasticsearchService = context.getBean(ElasticsearchService.class);

        // recreate index and reload data
        recreateIndexAndReloadData(elasticsearchService);
    }

    private static void recreateIndexAndReloadData(ElasticsearchService elasticsearchService) {
        // get ElasticsearchOperations bean
        ElasticsearchOperations elasticsearchOperations = elasticsearchService.getElasticsearchOperations();

        // delete existing index
        IndexOperations indexOperations = elasticsearchOperations.indexOps(elasticsearchService.getEntityClass());
        if (indexOperations.exists()) {
            indexOperations.delete();
            log.info("Existing index deleted.");
        }

        // create new index
        indexOperations.create();
        indexOperations.putMapping();
        log.info("New index created.");

        // load data from MongoDB to Elasticsearch
        elasticsearchService.reloadProperties();
        log.info("Data reloaded from MongoDB to Elasticsearch.");
    }
}
