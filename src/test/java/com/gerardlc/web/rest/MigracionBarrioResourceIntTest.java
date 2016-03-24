package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.MigracionBarrio;
import com.gerardlc.repository.MigracionBarrioRepository;
import com.gerardlc.repository.search.MigracionBarrioSearchRepository;

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
 * Test class for the MigracionBarrioResource REST controller.
 *
 * @see MigracionBarrioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MigracionBarrioResourceIntTest {


    private static final Integer DEFAULT_NUMERO_PERSONAS = 0;
    private static final Integer UPDATED_NUMERO_PERSONAS = 1;

    private static final Integer DEFAULT_ANYO = 1;
    private static final Integer UPDATED_ANYO = 2;

    @Inject
    private MigracionBarrioRepository migracionBarrioRepository;

    @Inject
    private MigracionBarrioSearchRepository migracionBarrioSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMigracionBarrioMockMvc;

    private MigracionBarrio migracionBarrio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MigracionBarrioResource migracionBarrioResource = new MigracionBarrioResource();
        ReflectionTestUtils.setField(migracionBarrioResource, "migracionBarrioSearchRepository", migracionBarrioSearchRepository);
        ReflectionTestUtils.setField(migracionBarrioResource, "migracionBarrioRepository", migracionBarrioRepository);
        this.restMigracionBarrioMockMvc = MockMvcBuilders.standaloneSetup(migracionBarrioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        migracionBarrio = new MigracionBarrio();
        migracionBarrio.setNumeroPersonas(DEFAULT_NUMERO_PERSONAS);
        migracionBarrio.setAnyo(DEFAULT_ANYO);
    }

    @Test
    @Transactional
    public void createMigracionBarrio() throws Exception {
        int databaseSizeBeforeCreate = migracionBarrioRepository.findAll().size();

        // Create the MigracionBarrio

        restMigracionBarrioMockMvc.perform(post("/api/migracionBarrios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(migracionBarrio)))
                .andExpect(status().isCreated());

        // Validate the MigracionBarrio in the database
        List<MigracionBarrio> migracionBarrios = migracionBarrioRepository.findAll();
        assertThat(migracionBarrios).hasSize(databaseSizeBeforeCreate + 1);
        MigracionBarrio testMigracionBarrio = migracionBarrios.get(migracionBarrios.size() - 1);
        assertThat(testMigracionBarrio.getNumeroPersonas()).isEqualTo(DEFAULT_NUMERO_PERSONAS);
        assertThat(testMigracionBarrio.getAnyo()).isEqualTo(DEFAULT_ANYO);
    }

    @Test
    @Transactional
    public void checkNumeroPersonasIsRequired() throws Exception {
        int databaseSizeBeforeTest = migracionBarrioRepository.findAll().size();
        // set the field null
        migracionBarrio.setNumeroPersonas(null);

        // Create the MigracionBarrio, which fails.

        restMigracionBarrioMockMvc.perform(post("/api/migracionBarrios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(migracionBarrio)))
                .andExpect(status().isBadRequest());

        List<MigracionBarrio> migracionBarrios = migracionBarrioRepository.findAll();
        assertThat(migracionBarrios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMigracionBarrios() throws Exception {
        // Initialize the database
        migracionBarrioRepository.saveAndFlush(migracionBarrio);

        // Get all the migracionBarrios
        restMigracionBarrioMockMvc.perform(get("/api/migracionBarrios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(migracionBarrio.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroPersonas").value(hasItem(DEFAULT_NUMERO_PERSONAS)))
                .andExpect(jsonPath("$.[*].anyo").value(hasItem(DEFAULT_ANYO)));
    }

    @Test
    @Transactional
    public void getMigracionBarrio() throws Exception {
        // Initialize the database
        migracionBarrioRepository.saveAndFlush(migracionBarrio);

        // Get the migracionBarrio
        restMigracionBarrioMockMvc.perform(get("/api/migracionBarrios/{id}", migracionBarrio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(migracionBarrio.getId().intValue()))
            .andExpect(jsonPath("$.numeroPersonas").value(DEFAULT_NUMERO_PERSONAS))
            .andExpect(jsonPath("$.anyo").value(DEFAULT_ANYO));
    }

    @Test
    @Transactional
    public void getNonExistingMigracionBarrio() throws Exception {
        // Get the migracionBarrio
        restMigracionBarrioMockMvc.perform(get("/api/migracionBarrios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMigracionBarrio() throws Exception {
        // Initialize the database
        migracionBarrioRepository.saveAndFlush(migracionBarrio);

		int databaseSizeBeforeUpdate = migracionBarrioRepository.findAll().size();

        // Update the migracionBarrio
        migracionBarrio.setNumeroPersonas(UPDATED_NUMERO_PERSONAS);
        migracionBarrio.setAnyo(UPDATED_ANYO);

        restMigracionBarrioMockMvc.perform(put("/api/migracionBarrios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(migracionBarrio)))
                .andExpect(status().isOk());

        // Validate the MigracionBarrio in the database
        List<MigracionBarrio> migracionBarrios = migracionBarrioRepository.findAll();
        assertThat(migracionBarrios).hasSize(databaseSizeBeforeUpdate);
        MigracionBarrio testMigracionBarrio = migracionBarrios.get(migracionBarrios.size() - 1);
        assertThat(testMigracionBarrio.getNumeroPersonas()).isEqualTo(UPDATED_NUMERO_PERSONAS);
        assertThat(testMigracionBarrio.getAnyo()).isEqualTo(UPDATED_ANYO);
    }

    @Test
    @Transactional
    public void deleteMigracionBarrio() throws Exception {
        // Initialize the database
        migracionBarrioRepository.saveAndFlush(migracionBarrio);

		int databaseSizeBeforeDelete = migracionBarrioRepository.findAll().size();

        // Get the migracionBarrio
        restMigracionBarrioMockMvc.perform(delete("/api/migracionBarrios/{id}", migracionBarrio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MigracionBarrio> migracionBarrios = migracionBarrioRepository.findAll();
        assertThat(migracionBarrios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
