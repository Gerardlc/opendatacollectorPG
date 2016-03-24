package com.gerardlc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gerardlc.domain.DataSet;
import com.gerardlc.repository.DataSetRepository;
import com.gerardlc.repository.search.DataSetSearchRepository;
import com.gerardlc.web.rest.util.HeaderUtil;
import com.gerardlc.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DataSet.
 */
@RestController
@RequestMapping("/api")
public class DataSetResource {

    private final Logger log = LoggerFactory.getLogger(DataSetResource.class);
        
    @Inject
    private DataSetRepository dataSetRepository;
    
    @Inject
    private DataSetSearchRepository dataSetSearchRepository;
    
    /**
     * POST  /dataSets -> Create a new dataSet.
     */
    @RequestMapping(value = "/dataSets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataSet> createDataSet(@RequestBody DataSet dataSet) throws URISyntaxException {
        log.debug("REST request to save DataSet : {}", dataSet);
        if (dataSet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dataSet", "idexists", "A new dataSet cannot already have an ID")).body(null);
        }
        DataSet result = dataSetRepository.save(dataSet);
        dataSetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dataSets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dataSet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dataSets -> Updates an existing dataSet.
     */
    @RequestMapping(value = "/dataSets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataSet> updateDataSet(@RequestBody DataSet dataSet) throws URISyntaxException {
        log.debug("REST request to update DataSet : {}", dataSet);
        if (dataSet.getId() == null) {
            return createDataSet(dataSet);
        }
        DataSet result = dataSetRepository.save(dataSet);
        dataSetSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dataSet", dataSet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dataSets -> get all the dataSets.
     */
    @RequestMapping(value = "/dataSets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DataSet>> getAllDataSets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DataSets");
        Page<DataSet> page = dataSetRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dataSets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dataSets/:id -> get the "id" dataSet.
     */
    @RequestMapping(value = "/dataSets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataSet> getDataSet(@PathVariable Long id) {
        log.debug("REST request to get DataSet : {}", id);
        DataSet dataSet = dataSetRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(dataSet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dataSets/:id -> delete the "id" dataSet.
     */
    @RequestMapping(value = "/dataSets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDataSet(@PathVariable Long id) {
        log.debug("REST request to delete DataSet : {}", id);
        dataSetRepository.delete(id);
        dataSetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dataSet", id.toString())).build();
    }

    /**
     * SEARCH  /_search/dataSets/:query -> search for the dataSet corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/dataSets/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DataSet> searchDataSets(@PathVariable String query) {
        log.debug("REST request to search DataSets for query {}", query);
        return StreamSupport
            .stream(dataSetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
