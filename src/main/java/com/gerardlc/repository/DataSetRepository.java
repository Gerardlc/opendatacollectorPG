package com.gerardlc.repository;

import com.gerardlc.domain.DataSet;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the DataSet entity.
 */
public interface DataSetRepository extends JpaRepository<DataSet,Long> {

    @Query("select distinct dataSet from DataSet dataSet left join fetch dataSet.subCategorias")
    List<DataSet> findAllWithEagerRelationships();

    @Query("select dataSet from DataSet dataSet left join fetch dataSet.subCategorias where dataSet.id =:id")
    DataSet findOneWithEagerRelationships(@Param("id") Long id);

}
