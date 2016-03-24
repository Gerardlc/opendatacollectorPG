package com.gerardlc.repository.search;

import com.gerardlc.domain.DataSet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DataSet entity.
 */
public interface DataSetSearchRepository extends ElasticsearchRepository<DataSet, Long> {
}
