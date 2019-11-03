package com.tl.cuentas.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"numeroCliente", "saldoDisponible",
                               "consumosPendientesPago", "fechaCreacion", "fechaModificacion"},
                      allowGetters = true,
                      ignoreUnknown = true)
public class Cuenta {

    private Long numeroCuenta;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "numeroCliente")
    @JsonIdentityReference(alwaysAsId = true)
    private Cliente numeroCliente;
    private Long saldoDisponible;
    private Long consumosPendientesPago;
    private Parametro.EstadoCuenta estado;
    private Date fechaCreacion;
    private Date fechaModificacion;
}
