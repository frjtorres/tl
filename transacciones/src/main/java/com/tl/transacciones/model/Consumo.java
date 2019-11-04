package com.tl.transacciones.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Consumo extends Transaccion {

    private Parametro.ModalidadConsumo modalidad;
    private Parametro.ServicioConsumo servicio;
}
