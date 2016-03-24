package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.Ciudad;
import com.gerardlc.repository.CiudadRepository;
import com.gerardlc.repository.search.CiudadSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CiudadResource REST controller.
 *
 * @see CiudadResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CiudadResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_PAIS = "AAAAA";
    private static final String UPDATED_PAIS = "BBBBB";

    @Inject
    private CiudadRepository ciudadRepository;

    @Inject
    private CiudadSearchRepository ciudadSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCiudadMockMvc;

    private Ciudad ciudad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CiudadResource ciudadResource = new CiudadResource();
        ReflectionTestUtils.setField(ciudadResource, "ciudadSearchRepository", ciudadSearchRepository);
        ReflectionTestUtils.setField(ciudadResource, "ciudadRepository", ciudadRepository);
        this.restCiudadMockMvc = MockMvcBuilders.standaloneSetup(ciudadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ciudad = new Ciudad();
        ciudad.setNombre(DEFAULT_NOMBRE);
        ciudad.setPais(DEFAULT_PAIS);
    }

    @Test
    @Transactional
    public void createCiudad() throws Exception {
        int databaseSizeBeforeCreate = ciudadRepository.findAll().size();

        // Create the Ciudad

        restCiudadMockMvc.perform(post("/api/ciudads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ciudad)))
                .andExpect(status().isCreated());

        // Validate the Ciudad in the database
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeCreate + 1);
        Ciudad testCiudad = ciudads.get(ciudads.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCiudad.getPais()).isEqualTo(DEFAULT_PAIS);
    }

    @Test
    @Transactional
    public void getAllCiudads() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudads
        restCiudadMockMvc.perform(get("/api/ciudads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS.toString())));
    }

    @Test
    @Transactional
    public void getCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

        // Get the ciudad
        restCiudadMockMvc.perform(get("/api/ciudads/{id}", ciudad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ciudad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCiudad() throws Exception {
        // Get the ciudad
        restCiudadMockMvc.perform(get("/api/ciudads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

		int databaseSizeBeforeUpdate = ciudadRepository.findAll().size();

        // Update the ciudad
        ciudad.setNombre(UPDATED_NOMBRE);
        ciudad.setPais(UPDATED_PAIS);

        restCiudadMockMvc.perform(put("/api/ciudads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ciudad)))
                .andExpect(status().isOk());

        // Validate the Ciudad in the database
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeUpdate);
        Ciudad testCiudad = ciudads.get(ciudads.size() - 1);
        assertThat(testCiudad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCiudad.getPais()).isEqualTo(UPDATED_PAIS);
    }

    @Test
    @Transactional
    public void deleteCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

		int databaseSizeBeforeDelete = ciudadRepository.findAll().size();

        // Get the ciudad
        restCiudadMockMvc.perform(delete("/api/ciudads/{id}", ciudad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
