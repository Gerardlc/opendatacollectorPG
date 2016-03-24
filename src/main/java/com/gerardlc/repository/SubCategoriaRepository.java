package com.gerardlc.repository;

import com.gerardlc.domain.SubCategoria;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SubCategoria entity.
 */
public interface SubCategoriaRepository extends JpaRepository<SubCategoria,Long> {

}
