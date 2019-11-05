package com.tl.transacciones.repository;

import com.tl.transacciones.entity.RecargaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecargaRepository extends JpaRepository<RecargaEntity, Long> {}
