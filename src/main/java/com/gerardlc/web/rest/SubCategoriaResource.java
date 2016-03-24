package com.gerardlc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gerardlc.domain.SubCategoria;
import com.gerardlc.repository.SubCategoriaRepository;
import com.gerardlc.repository.search.SubCategoriaSearchRepository;
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
 * REST controller for managing SubCategoria.
 */
@RestController
@RequestMapping("/api")
public class SubCategoriaResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoriaResource.class);
        
    @Inject
    private SubCategoriaRepository subCategoriaRepository;
    
    @Inject
    private SubCategoriaSearchRepository subCategoriaSearchRepository;
    
    /**
     * POST  /subCategorias -> Create a new subCategoria.
     */
    @RequestMapping(value = "/subCategorias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubCategoria> createSubCategoria(@RequestBody SubCategoria subCategoria) throws URISyntaxException {
        log.debug("REST request to save SubCategoria : {}", subCategoria);
        if (subCategoria.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subCategoria", "idexists", "A new subCategoria cannot already have an ID")).body(null);
        }
        SubCategoria result = subCategoriaRepository.save(subCategoria);
        subCategoriaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/subCategorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subCategoria", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subCategorias -> Updates an existing subCategoria.
     */
    @RequestMapping(value = "/subCategorias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubCategoria> updateSubCategoria(@RequestBody SubCategoria subCategoria) throws URISyntaxException {
        log.debug("REST request to update SubCategoria : {}", subCategoria);
        if (subCategoria.getId() == null) {
            return createSubCategoria(subCategoria);
        }
        SubCategoria result = subCategoriaRepository.save(subCategoria);
        subCategoriaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subCategoria", subCategoria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subCategorias -> get all the subCategorias.
     */
    @RequestMapping(value = "/subCategorias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SubCategoria>> getAllSubCategorias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SubCategorias");
        Page<SubCategoria> page = subCategoriaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subCategorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /subCategorias/:id -> get the "id" subCategoria.
     */
    @RequestMapping(value = "/subCategorias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubCategoria> getSubCategoria(@PathVariable Long id) {
        log.debug("REST request to get SubCategoria : {}", id);
        SubCategoria subCategoria = subCategoriaRepository.findOne(id);
        return Optional.ofNullable(subCategoria)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subCategorias/:id -> delete the "id" subCategoria.
     */
    @RequestMapping(value = "/subCategorias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubCategoria(@PathVariable Long id) {
        log.debug("REST request to delete SubCategoria : {}", id);
        subCategoriaRepository.delete(id);
        subCategoriaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subCategoria", id.toString())).build();
    }

    /**
     * SEARCH  /_search/subCategorias/:query -> search for the subCategoria corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/subCategorias/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SubCategoria> searchSubCategorias(@PathVariable String query) {
        log.debug("REST request to search SubCategorias for query {}", query);
        return StreamSupport
            .stream(subCategoriaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
