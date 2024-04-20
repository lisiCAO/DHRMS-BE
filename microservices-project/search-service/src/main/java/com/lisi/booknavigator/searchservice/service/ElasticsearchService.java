package com.lisi.booknavigator.searchservice.service;

import com.lisi.booknavigator.searchservice.entity.Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
@Setter
public class ElasticsearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Property> search(String processQuery) {
        Criteria criteria = new Criteria("ownerUserId").contains(processQuery)
                .or(new Criteria("address").contains(processQuery))
                .or(new Criteria("postcode").contains(processQuery))
                .or(new Criteria("propertyType").contains(processQuery))
                .or(new Criteria("propertyDescription").contains(processQuery))
                .or(new Criteria("status").contains(processQuery))
                .or(new Criteria("id").contains(processQuery));

        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<Property> searchHits = elasticsearchOperations.search(searchQuery, Property.class);
        log.info("elasticsearchOperations.search({}, Property.class) ok", processQuery);
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public void saveOrUpdateProperty(Property property) {
        elasticsearchOperations.save(property);
        log.info("elasticsearchOperations.save {} ok", property.getId());
    }

    public void deleteProperty(String propertyId) {

        elasticsearchOperations.delete(propertyId, Property.class);
        log.info("elasticsearchOperations.delete({}, Property.class) ok", propertyId);
    }

    public Class<?> getEntityClass() {
        // return Property.class;
        return Property.class;
    }

    public void reloadProperties() {
        // get data from MongoDB
        List<Property> documents = mongoTemplate.findAll(Property.class);
        log.info("read data from MongoDB, size: {}", documents.size());

        // save data to Elasticsearch
        documents.forEach(document -> {
            elasticsearchOperations.save(document, IndexCoordinates.of("properties"));
            log.info("elasticsearch.save({}, Index(\"properties\")) ok", document.getId());
        });
        log.info("saved data to Elasticsearch");
    }

}
