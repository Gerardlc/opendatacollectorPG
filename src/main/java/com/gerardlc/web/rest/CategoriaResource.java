package com.gerardlc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gerardlc.domain.Categoria;
import com.gerardlc.repository.CategoriaRepository;
import com.gerardlc.repository.search.CategoriaSearchRepository;
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
 * REST controller for managing Categoria.
 */
@RestController
@RequestMapping("/api")
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);
        
    @Inject
    private CategoriaRepository categoriaRepository;
    
    @Inject
    private CategoriaSearchRepository categoriaSearchRepository;
    
    /**
     * POST  /categorias -> Create a new categoria.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoria);
        if (categoria.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categoria", "idexists", "A new categoria cannot already have an ID")).body(null);
        }
        Categoria result = categoriaRepository.save(categoria);
        categoriaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoria", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorias -> Updates an existing categoria.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> updateCategoria(@RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}", categoria);
        if (categoria.getId() == null) {
            return createCategoria(categoria);
        }
        Categoria result = categoriaRepository.save(categoria);
        categoriaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoria", categoria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorias -> get all the categorias.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Categoria>> getAllCategorias(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Categorias");
        Page<Categoria> page = categoriaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categorias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categorias/:id -> get the "id" categoria.
     */
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Categoria categoria = categoriaRepository.findOne(id);
        return Optional.ofNullable(categoria)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorias/:id -> delete the "id" categoria.
     */
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaRepository.delete(id);
        categoriaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoria", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categorias/:query -> search for the categoria corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/categorias/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categoria> searchCategorias(@PathVariable String query) {
        log.debug("REST request to search Categorias for query {}", query);
        return StreamSupport
            .stream(categoriaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
