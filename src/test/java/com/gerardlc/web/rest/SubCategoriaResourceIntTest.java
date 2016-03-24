package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.SubCategoria;
import com.gerardlc.repository.SubCategoriaRepository;
import com.gerardlc.repository.search.SubCategoriaSearchRepository;

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
 * Test class for the SubCategoriaResource REST controller.
 *
 * @see SubCategoriaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SubCategoriaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private SubCategoriaRepository subCategoriaRepository;

    @Inject
    private SubCategoriaSearchRepository subCategoriaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSubCategoriaMockMvc;

    private SubCategoria subCategoria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubCategoriaResource subCategoriaResource = new SubCategoriaResource();
        ReflectionTestUtils.setField(subCategoriaResource, "subCategoriaSearchRepository", subCategoriaSearchRepository);
        ReflectionTestUtils.setField(subCategoriaResource, "subCategoriaRepository", subCategoriaRepository);
        this.restSubCategoriaMockMvc = MockMvcBuilders.standaloneSetup(subCategoriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        subCategoria = new SubCategoria();
        subCategoria.setNombre(DEFAULT_NOMBRE);
        subCategoria.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createSubCategoria() throws Exception {
        int databaseSizeBeforeCreate = subCategoriaRepository.findAll().size();

        // Create the SubCategoria

        restSubCategoriaMockMvc.perform(post("/api/subCategorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subCategoria)))
                .andExpect(status().isCreated());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategorias = subCategoriaRepository.findAll();
        assertThat(subCategorias).hasSize(databaseSizeBeforeCreate + 1);
        SubCategoria testSubCategoria = subCategorias.get(subCategorias.size() - 1);
        assertThat(testSubCategoria.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSubCategoria.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllSubCategorias() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        // Get all the subCategorias
        restSubCategoriaMockMvc.perform(get("/api/subCategorias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subCategoria.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        // Get the subCategoria
        restSubCategoriaMockMvc.perform(get("/api/subCategorias/{id}", subCategoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(subCategoria.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubCategoria() throws Exception {
        // Get the subCategoria
        restSubCategoriaMockMvc.perform(get("/api/subCategorias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

		int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();

        // Update the subCategoria
        subCategoria.setNombre(UPDATED_NOMBRE);
        subCategoria.setDescripcion(UPDATED_DESCRIPCION);

        restSubCategoriaMockMvc.perform(put("/api/subCategorias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subCategoria)))
                .andExpect(status().isOk());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategorias = subCategoriaRepository.findAll();
        assertThat(subCategorias).hasSize(databaseSizeBeforeUpdate);
        SubCategoria testSubCategoria = subCategorias.get(subCategorias.size() - 1);
        assertThat(testSubCategoria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSubCategoria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

		int databaseSizeBeforeDelete = subCategoriaRepository.findAll().size();

        // Get the subCategoria
        restSubCategoriaMockMvc.perform(delete("/api/subCategorias/{id}", subCategoria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SubCategoria> subCategorias = subCategoriaRepository.findAll();
        assertThat(subCategorias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
