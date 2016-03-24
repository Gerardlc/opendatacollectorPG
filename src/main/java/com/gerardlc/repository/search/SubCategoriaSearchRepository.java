package com.gerardlc.repository.search;

import com.gerardlc.domain.SubCategoria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SubCategoria entity.
 */
public interface SubCategoriaSearchRepository extends ElasticsearchRepository<SubCategoria, Long> {
}
