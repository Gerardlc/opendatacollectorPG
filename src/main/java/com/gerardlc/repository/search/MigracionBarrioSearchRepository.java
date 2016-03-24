package com.gerardlc.repository.search;

import com.gerardlc.domain.MigracionBarrio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MigracionBarrio entity.
 */
public interface MigracionBarrioSearchRepository extends ElasticsearchRepository<MigracionBarrio, Long> {
}
