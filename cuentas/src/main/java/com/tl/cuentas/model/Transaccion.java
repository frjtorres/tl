package com.tl.cuentas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaccion {

    private Parametro.TipoTransaccion tipo;
    private Long monto;
}
