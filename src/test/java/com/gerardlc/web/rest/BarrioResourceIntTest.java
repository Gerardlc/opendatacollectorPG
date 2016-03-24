package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.Barrio;
import com.gerardlc.repository.BarrioRepository;
import com.gerardlc.repository.search.BarrioSearchRepository;

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
 * Test class for the BarrioResource REST controller.
 *
 * @see BarrioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BarrioResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_NOMBRE_CSV_ORIGEN = "AAAAA";
    private static final String UPDATED_NOMBRE_CSV_ORIGEN = "BBBBB";
    private static final String DEFAULT_NOMBRE_CSV_DESTINO = "AAAAA";
    private static final String UPDATED_NOMBRE_CSV_DESTINO = "BBBBB";

    @Inject
    private BarrioRepository barrioRepository;

    @Inject
    private BarrioSearchRepository barrioSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBarrioMockMvc;

    private Barrio barrio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BarrioResource barrioResource = new BarrioResource();
        ReflectionTestUtils.setField(barrioResource, "barrioSearchRepository", barrioSearchRepository);
        ReflectionTestUtils.setField(barrioResource, "barrioRepository", barrioRepository);
        this.restBarrioMockMvc = MockMvcBuilders.standaloneSetup(barrioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        barrio = new Barrio();
        barrio.setNombre(DEFAULT_NOMBRE);
        barrio.setNombreCsvOrigen(DEFAULT_NOMBRE_CSV_ORIGEN);
        barrio.setNombreCsvDestino(DEFAULT_NOMBRE_CSV_DESTINO);
    }

    @Test
    @Transactional
    public void createBarrio() throws Exception {
        int databaseSizeBeforeCreate = barrioRepository.findAll().size();

        // Create the Barrio

        restBarrioMockMvc.perform(post("/api/barrios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(barrio)))
                .andExpect(status().isCreated());

        // Validate the Barrio in the database
        List<Barrio> barrios = barrioRepository.findAll();
        assertThat(barrios).hasSize(databaseSizeBeforeCreate + 1);
        Barrio testBarrio = barrios.get(barrios.size() - 1);
        assertThat(testBarrio.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testBarrio.getNombreCsvOrigen()).isEqualTo(DEFAULT_NOMBRE_CSV_ORIGEN);
        assertThat(testBarrio.getNombreCsvDestino()).isEqualTo(DEFAULT_NOMBRE_CSV_DESTINO);
    }

    @Test
    @Transactional
    public void getAllBarrios() throws Exception {
        // Initialize the database
        barrioRepository.saveAndFlush(barrio);

        // Get all the barrios
        restBarrioMockMvc.perform(get("/api/barrios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(barrio.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].nombreCsvOrigen").value(hasItem(DEFAULT_NOMBRE_CSV_ORIGEN.toString())))
                .andExpect(jsonPath("$.[*].nombreCsvDestino").value(hasItem(DEFAULT_NOMBRE_CSV_DESTINO.toString())));
    }

    @Test
    @Transactional
    public void getBarrio() throws Exception {
        // Initialize the database
        barrioRepository.saveAndFlush(barrio);

        // Get the barrio
        restBarrioMockMvc.perform(get("/api/barrios/{id}", barrio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(barrio.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.nombreCsvOrigen").value(DEFAULT_NOMBRE_CSV_ORIGEN.toString()))
            .andExpect(jsonPath("$.nombreCsvDestino").value(DEFAULT_NOMBRE_CSV_DESTINO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarrio() throws Exception {
        // Get the barrio
        restBarrioMockMvc.perform(get("/api/barrios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarrio() throws Exception {
        // Initialize the database
        barrioRepository.saveAndFlush(barrio);

		int databaseSizeBeforeUpdate = barrioRepository.findAll().size();

        // Update the barrio
        barrio.setNombre(UPDATED_NOMBRE);
        barrio.setNombreCsvOrigen(UPDATED_NOMBRE_CSV_ORIGEN);
        barrio.setNombreCsvDestino(UPDATED_NOMBRE_CSV_DESTINO);

        restBarrioMockMvc.perform(put("/api/barrios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(barrio)))
                .andExpect(status().isOk());

        // Validate the Barrio in the database
        List<Barrio> barrios = barrioRepository.findAll();
        assertThat(barrios).hasSize(databaseSizeBeforeUpdate);
        Barrio testBarrio = barrios.get(barrios.size() - 1);
        assertThat(testBarrio.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testBarrio.getNombreCsvOrigen()).isEqualTo(UPDATED_NOMBRE_CSV_ORIGEN);
        assertThat(testBarrio.getNombreCsvDestino()).isEqualTo(UPDATED_NOMBRE_CSV_DESTINO);
    }

    @Test
    @Transactional
    public void deleteBarrio() throws Exception {
        // Initialize the database
        barrioRepository.saveAndFlush(barrio);

		int databaseSizeBeforeDelete = barrioRepository.findAll().size();

        // Get the barrio
        restBarrioMockMvc.perform(delete("/api/barrios/{id}", barrio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Barrio> barrios = barrioRepository.findAll();
        assertThat(barrios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
