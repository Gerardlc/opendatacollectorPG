package com.gerardlc.repository.search;

import com.gerardlc.domain.Ciudad;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ciudad entity.
 */
public interface CiudadSearchRepository extends ElasticsearchRepository<Ciudad, Long> {
}
