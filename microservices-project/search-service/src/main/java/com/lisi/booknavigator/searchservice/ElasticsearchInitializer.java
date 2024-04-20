package com.lisi.booknavigator.searchservice;

import com.lisi.booknavigator.searchservice.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElasticsearchInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final ElasticsearchService elasticsearchService;

    @Autowired
    public ElasticsearchInitializer(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recreateIndexAndReloadData();
    }

    private void recreateIndexAndReloadData() {
        ElasticsearchOperations elasticsearchOperations = elasticsearchService.getElasticsearchOperations();
        IndexOperations indexOperations = elasticsearchOperations.indexOps(elasticsearchService.getEntityClass());

        // Delete existing index
        if (indexOperations.exists()) {
            indexOperations.delete();
            log.info("Existing index deleted.");
        }

        // Create new index and mapping
        indexOperations.create();
        indexOperations.putMapping();
        log.info("New index created.");

        // Load data from MongoDB to Elasticsearch
        elasticsearchService.reloadProperties();
        log.info("Data reloaded from MongoDB to Elasticsearch.");
    }
}
