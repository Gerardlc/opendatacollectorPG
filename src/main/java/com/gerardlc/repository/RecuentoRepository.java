package com.gerardlc.repository;

import com.gerardlc.domain.Recuento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Recuento entity.
 */
public interface RecuentoRepository extends JpaRepository<Recuento,Long> {

}
