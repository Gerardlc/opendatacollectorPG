package com.gerardlc.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gerardlc.domain.Recuento;
import com.gerardlc.repository.RecuentoRepository;
import com.gerardlc.repository.search.RecuentoSearchRepository;
import com.gerardlc.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Recuento.
 */
@RestController
@RequestMapping("/api")
public class RecuentoResource {

    private final Logger log = LoggerFactory.getLogger(RecuentoResource.class);
        
    @Inject
    private RecuentoRepository recuentoRepository;
    
    @Inject
    private RecuentoSearchRepository recuentoSearchRepository;
    
    /**
     * POST  /recuentos -> Create a new recuento.
     */
    @RequestMapping(value = "/recuentos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recuento> createRecuento(@RequestBody Recuento recuento) throws URISyntaxException {
        log.debug("REST request to save Recuento : {}", recuento);
        if (recuento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recuento", "idexists", "A new recuento cannot already have an ID")).body(null);
        }
        Recuento result = recuentoRepository.save(recuento);
        recuentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recuentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recuento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recuentos -> Updates an existing recuento.
     */
    @RequestMapping(value = "/recuentos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recuento> updateRecuento(@RequestBody Recuento recuento) throws URISyntaxException {
        log.debug("REST request to update Recuento : {}", recuento);
        if (recuento.getId() == null) {
            return createRecuento(recuento);
        }
        Recuento result = recuentoRepository.save(recuento);
        recuentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recuento", recuento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recuentos -> get all the recuentos.
     */
    @RequestMapping(value = "/recuentos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Recuento> getAllRecuentos() {
        log.debug("REST request to get all Recuentos");
        return recuentoRepository.findAll();
            }

    /**
     * GET  /recuentos/:id -> get the "id" recuento.
     */
    @RequestMapping(value = "/recuentos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recuento> getRecuento(@PathVariable Long id) {
        log.debug("REST request to get Recuento : {}", id);
        Recuento recuento = recuentoRepository.findOne(id);
        return Optional.ofNullable(recuento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recuentos/:id -> delete the "id" recuento.
     */
    @RequestMapping(value = "/recuentos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecuento(@PathVariable Long id) {
        log.debug("REST request to delete Recuento : {}", id);
        recuentoRepository.delete(id);
        recuentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recuento", id.toString())).build();
    }

    /**
     * SEARCH  /_search/recuentos/:query -> search for the recuento corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/recuentos/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Recuento> searchRecuentos(@PathVariable String query) {
        log.debug("REST request to search Recuentos for query {}", query);
        return StreamSupport
            .stream(recuentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
