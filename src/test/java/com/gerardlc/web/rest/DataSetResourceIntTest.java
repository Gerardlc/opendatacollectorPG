package com.gerardlc.web.rest;

import com.gerardlc.Application;
import com.gerardlc.domain.DataSet;
import com.gerardlc.repository.DataSetRepository;
import com.gerardlc.repository.search.DataSetSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DataSetResource REST controller.
 *
 * @see DataSetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DataSetResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ENLACE_DESCARGA = "AAAAA";
    private static final String UPDATED_ENLACE_DESCARGA = "BBBBB";

    @Inject
    private DataSetRepository dataSetRepository;

    @Inject
    private DataSetSearchRepository dataSetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDataSetMockMvc;

    private DataSet dataSet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataSetResource dataSetResource = new DataSetResource();
        ReflectionTestUtils.setField(dataSetResource, "dataSetSearchRepository", dataSetSearchRepository);
        ReflectionTestUtils.setField(dataSetResource, "dataSetRepository", dataSetRepository);
        this.restDataSetMockMvc = MockMvcBuilders.standaloneSetup(dataSetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dataSet = new DataSet();
        dataSet.setNombre(DEFAULT_NOMBRE);
        dataSet.setDescripcion(DEFAULT_DESCRIPCION);
        dataSet.setFecha(DEFAULT_FECHA);
        dataSet.setEnlaceDescarga(DEFAULT_ENLACE_DESCARGA);
    }

    @Test
    @Transactional
    public void createDataSet() throws Exception {
        int databaseSizeBeforeCreate = dataSetRepository.findAll().size();

        // Create the DataSet

        restDataSetMockMvc.perform(post("/api/dataSets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataSet)))
                .andExpect(status().isCreated());

        // Validate the DataSet in the database
        List<DataSet> dataSets = dataSetRepository.findAll();
        assertThat(dataSets).hasSize(databaseSizeBeforeCreate + 1);
        DataSet testDataSet = dataSets.get(dataSets.size() - 1);
        assertThat(testDataSet.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDataSet.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testDataSet.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testDataSet.getEnlaceDescarga()).isEqualTo(DEFAULT_ENLACE_DESCARGA);
    }

    @Test
    @Transactional
    public void getAllDataSets() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get all the dataSets
        restDataSetMockMvc.perform(get("/api/dataSets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dataSet.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
                .andExpect(jsonPath("$.[*].enlaceDescarga").value(hasItem(DEFAULT_ENLACE_DESCARGA.toString())));
    }

    @Test
    @Transactional
    public void getDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

        // Get the dataSet
        restDataSetMockMvc.perform(get("/api/dataSets/{id}", dataSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dataSet.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.enlaceDescarga").value(DEFAULT_ENLACE_DESCARGA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataSet() throws Exception {
        // Get the dataSet
        restDataSetMockMvc.perform(get("/api/dataSets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

		int databaseSizeBeforeUpdate = dataSetRepository.findAll().size();

        // Update the dataSet
        dataSet.setNombre(UPDATED_NOMBRE);
        dataSet.setDescripcion(UPDATED_DESCRIPCION);
        dataSet.setFecha(UPDATED_FECHA);
        dataSet.setEnlaceDescarga(UPDATED_ENLACE_DESCARGA);

        restDataSetMockMvc.perform(put("/api/dataSets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataSet)))
                .andExpect(status().isOk());

        // Validate the DataSet in the database
        List<DataSet> dataSets = dataSetRepository.findAll();
        assertThat(dataSets).hasSize(databaseSizeBeforeUpdate);
        DataSet testDataSet = dataSets.get(dataSets.size() - 1);
        assertThat(testDataSet.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDataSet.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testDataSet.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testDataSet.getEnlaceDescarga()).isEqualTo(UPDATED_ENLACE_DESCARGA);
    }

    @Test
    @Transactional
    public void deleteDataSet() throws Exception {
        // Initialize the database
        dataSetRepository.saveAndFlush(dataSet);

		int databaseSizeBeforeDelete = dataSetRepository.findAll().size();

        // Get the dataSet
        restDataSetMockMvc.perform(delete("/api/dataSets/{id}", dataSet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DataSet> dataSets = dataSetRepository.findAll();
        assertThat(dataSets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
