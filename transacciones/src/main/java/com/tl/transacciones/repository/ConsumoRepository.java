package com.tl.transacciones.repository;

import com.tl.transacciones.entity.ConsumoEntity;
import com.tl.transacciones.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumoRepository extends JpaRepository<ConsumoEntity, Long> {

    @Query("SELECT ca " +
           "FROM ConsumoEntity ca " +
           "WHERE ca.numeroCuenta = :numeroCuenta " +
           "AND ca.fechaCreacion >= (SELECT MAX(cb.fechaCreacion) " +
                                      "FROM ConsumoEntity cb " +
                                      "WHERE cb.modalidad = 'ENT' " +
                                      "AND cb.numeroCuenta = :numeroCuenta " +
                                      "AND cb.fechaCreacion >= current_date() - 1)")
    List<ConsumoEntity> consumosViajeReciente(@Param("numeroCuenta") CuentaEntity numeroCuenta);
}
