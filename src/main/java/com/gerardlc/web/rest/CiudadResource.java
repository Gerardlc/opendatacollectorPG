package com.gerardlc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gerardlc.domain.Ciudad;
import com.gerardlc.repository.CiudadRepository;
import com.gerardlc.repository.search.CiudadSearchRepository;
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
 * REST controller for managing Ciudad.
 */
@RestController
@RequestMapping("/api")
public class CiudadResource {

    private final Logger log = LoggerFactory.getLogger(CiudadResource.class);
        
    @Inject
    private CiudadRepository ciudadRepository;
    
    @Inject
    private CiudadSearchRepository ciudadSearchRepository;
    
    /**
     * POST  /ciudads -> Create a new ciudad.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> createCiudad(@RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to save Ciudad : {}", ciudad);
        if (ciudad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ciudad", "idexists", "A new ciudad cannot already have an ID")).body(null);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        ciudadSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ciudads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ciudad", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ciudads -> Updates an existing ciudad.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> updateCiudad(@RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to update Ciudad : {}", ciudad);
        if (ciudad.getId() == null) {
            return createCiudad(ciudad);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        ciudadSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ciudad", ciudad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ciudads -> get all the ciudads.
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ciudad>> getAllCiudads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ciudads");
        Page<Ciudad> page = ciudadRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ciudads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ciudads/:id -> get the "id" ciudad.
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> getCiudad(@PathVariable Long id) {
        log.debug("REST request to get Ciudad : {}", id);
        Ciudad ciudad = ciudadRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(ciudad)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ciudads/:id -> delete the "id" ciudad.
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        log.debug("REST request to delete Ciudad : {}", id);
        ciudadRepository.delete(id);
        ciudadSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ciudad", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ciudads/:query -> search for the ciudad corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/ciudads/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ciudad> searchCiudads(@PathVariable String query) {
        log.debug("REST request to search Ciudads for query {}", query);
        return StreamSupport
            .stream(ciudadSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
