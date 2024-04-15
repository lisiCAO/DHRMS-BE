package com.lisi.booknavigator.searchservice.service;

import com.lisi.booknavigator.searchservice.event.PropertyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PropertyEventHandler {

    private final ElasticsearchService elasticsearchService;

    @Autowired
    public PropertyEventHandler(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @KafkaListener(topics = "propertiesTopic", groupId = "search-service-group")
    public void listenPropertyChanges(PropertyEvent event) {
        switch (event.getEventType()) {
            case CREATE:
            case UPDATE:
                elasticsearchService.saveOrUpdateProperty(event.getElasticProperty());
                break;
            case DELETE:
                elasticsearchService.deleteProperty(event.getPropertyId());
                break;
        }
    }
}
