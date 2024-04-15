package com.lisi.booknavigator.searchservice.service;

import com.lisi.booknavigator.searchservice.entity.ElasticProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticProperty> search(String processQuery) {
        Criteria criteria = new Criteria("ownerUserId").contains(processQuery)
                .or(new Criteria("address").contains(processQuery))
                .or(new Criteria("postcode").contains(processQuery))
                .or(new Criteria("propertyType").contains(processQuery))
                .or(new Criteria("propertyDescription").contains(processQuery))
                .or(new Criteria("status").contains(processQuery));

        Query searchQuery = new CriteriaQuery(criteria);

        SearchHits<ElasticProperty> searchHits = elasticsearchOperations.search(searchQuery, ElasticProperty.class);
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public void saveOrUpdateProperty(ElasticProperty elasticProperty) {
        elasticsearchOperations.save(elasticProperty);
    }

    public void deleteProperty(String propertyId) {
        elasticsearchOperations.delete(propertyId, ElasticProperty.class);
    }
}
