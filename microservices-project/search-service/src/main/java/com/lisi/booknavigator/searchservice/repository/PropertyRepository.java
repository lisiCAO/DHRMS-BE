package com.lisi.booknavigator.searchservice.repository;

import com.lisi.booknavigator.searchservice.entity.ElasticProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PropertyRepository extends ElasticsearchRepository<ElasticProperty, String> {
}
