package com.lisi.booknavigator.searchservice.service;

import com.lisi.booknavigator.searchservice.event.PropertyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PropertyEventHandler {

    private final ElasticsearchService elasticsearchService;

    @Autowired
    public PropertyEventHandler(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @KafkaListener(topics = "propertiesTopic", groupId = "search-service-group")
    public void listenPropertyChanges(PropertyEvent event) {
        if (event == null || event.getProperty() == null) {
            log.error("Received null event or null property");
            return; // Skip processing for this message
        }
        switch (event.getEventType()) {
            case CREATE:
            case UPDATE:
                elasticsearchService.saveOrUpdateProperty(event.getProperty());
                break;
            case DELETE:
                elasticsearchService.deleteProperty(event.getPropertyId());
                break;
        }
    }
}
