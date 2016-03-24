package com.gerardlc.repository;

import com.gerardlc.domain.Barrio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Barrio entity.
 */
public interface BarrioRepository extends JpaRepository<Barrio,Long> {

}
