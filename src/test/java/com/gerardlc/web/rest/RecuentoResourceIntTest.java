package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.Recuento;
import com.gerardlc.repository.RecuentoRepository;
import com.gerardlc.repository.search.RecuentoSearchRepository;

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
 * Test class for the RecuentoResource REST controller.
 *
 * @see RecuentoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RecuentoResourceIntTest {


    @Inject
    private RecuentoRepository recuentoRepository;

    @Inject
    private RecuentoSearchRepository recuentoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRecuentoMockMvc;

    private Recuento recuento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecuentoResource recuentoResource = new RecuentoResource();
        ReflectionTestUtils.setField(recuentoResource, "recuentoSearchRepository", recuentoSearchRepository);
        ReflectionTestUtils.setField(recuentoResource, "recuentoRepository", recuentoRepository);
        this.restRecuentoMockMvc = MockMvcBuilders.standaloneSetup(recuentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        recuento = new Recuento();
    }

    @Test
    @Transactional
    public void createRecuento() throws Exception {
        int databaseSizeBeforeCreate = recuentoRepository.findAll().size();

        // Create the Recuento

        restRecuentoMockMvc.perform(post("/api/recuentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recuento)))
                .andExpect(status().isCreated());

        // Validate the Recuento in the database
        List<Recuento> recuentos = recuentoRepository.findAll();
        assertThat(recuentos).hasSize(databaseSizeBeforeCreate + 1);
        Recuento testRecuento = recuentos.get(recuentos.size() - 1);
    }

    @Test
    @Transactional
    public void getAllRecuentos() throws Exception {
        // Initialize the database
        recuentoRepository.saveAndFlush(recuento);

        // Get all the recuentos
        restRecuentoMockMvc.perform(get("/api/recuentos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recuento.getId().intValue())));
    }

    @Test
    @Transactional
    public void getRecuento() throws Exception {
        // Initialize the database
        recuentoRepository.saveAndFlush(recuento);

        // Get the recuento
        restRecuentoMockMvc.perform(get("/api/recuentos/{id}", recuento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(recuento.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecuento() throws Exception {
        // Get the recuento
        restRecuentoMockMvc.perform(get("/api/recuentos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecuento() throws Exception {
        // Initialize the database
        recuentoRepository.saveAndFlush(recuento);

		int databaseSizeBeforeUpdate = recuentoRepository.findAll().size();

        // Update the recuento

        restRecuentoMockMvc.perform(put("/api/recuentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recuento)))
                .andExpect(status().isOk());

        // Validate the Recuento in the database
        List<Recuento> recuentos = recuentoRepository.findAll();
        assertThat(recuentos).hasSize(databaseSizeBeforeUpdate);
        Recuento testRecuento = recuentos.get(recuentos.size() - 1);
    }

    @Test
    @Transactional
    public void deleteRecuento() throws Exception {
        // Initialize the database
        recuentoRepository.saveAndFlush(recuento);

		int databaseSizeBeforeDelete = recuentoRepository.findAll().size();

        // Get the recuento
        restRecuentoMockMvc.perform(delete("/api/recuentos/{id}", recuento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Recuento> recuentos = recuentoRepository.findAll();
        assertThat(recuentos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
