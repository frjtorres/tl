package com.tl.transacciones.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = "modalidad",
                      allowGetters = true,
                      ignoreUnknown = true)
public class Consumo extends Transaccion {

    private Parametro.ModalidadConsumo modalidad;
    private Parametro.ServicioConsumo servicio;
}
