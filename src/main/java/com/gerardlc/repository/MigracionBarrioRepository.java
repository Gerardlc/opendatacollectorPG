package com.gerardlc.repository;

import com.gerardlc.domain.MigracionBarrio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MigracionBarrio entity.
 */
public interface MigracionBarrioRepository extends JpaRepository<MigracionBarrio,Long> {

}
