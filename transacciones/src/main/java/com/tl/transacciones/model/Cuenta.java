package com.tl.transacciones.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cuenta {

    private Long numeroCuenta;
    private Long saldoDisponible;
    private Long consumosPendientesPago;
    private Parametro.EstadoCuenta estado;
    private List<Transaccion> transacciones;

    public Cuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
