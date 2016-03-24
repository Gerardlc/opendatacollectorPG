package com.gerardlc.repository;

import com.gerardlc.domain.Ciudad;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Ciudad entity.
 */
public interface CiudadRepository extends JpaRepository<Ciudad,Long> {

    @Query("select distinct ciudad from Ciudad ciudad left join fetch ciudad.categorias")
    List<Ciudad> findAllWithEagerRelationships();

    @Query("select ciudad from Ciudad ciudad left join fetch ciudad.categorias where ciudad.id =:id")
    Ciudad findOneWithEagerRelationships(@Param("id") Long id);

}
