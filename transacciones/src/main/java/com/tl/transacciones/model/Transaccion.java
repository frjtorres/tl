package com.tl.transacciones.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"numeroTransaccion", "tipo",
                               "numeroCuenta", "fechaCreacion"},
                      allowGetters = true,
                      ignoreUnknown = true)
public class Transaccion {

    private Long numeroTransaccion;
    private String tipo;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "numeroCuenta")
    @JsonIdentityReference(alwaysAsId = true)
    private Cuenta numeroCuenta;
    private Long monto;

    private Date fechaCreacion;
}
