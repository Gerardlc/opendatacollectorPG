package com.gerardlc.repository.search;

import com.gerardlc.domain.Recuento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Recuento entity.
 */
public interface RecuentoSearchRepository extends ElasticsearchRepository<Recuento, Long> {
}
