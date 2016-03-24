package com.gerardlc.repository.search;

import com.gerardlc.domain.Barrio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Barrio entity.
 */
public interface BarrioSearchRepository extends ElasticsearchRepository<Barrio, Long> {
}
