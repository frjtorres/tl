package com.tl.cuentas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(value = {"fechaCreacion", "fechaModificacion"},
                      allowGetters = true,
                      ignoreUnknown = true)
public class Cliente {

    private Long numeroCliente;
    private Parametro.TipoId tipoId;
    private String numeroId;
    private String nombres;
    private String apellidos;

    @JsonIgnore
    private Parametro.EstadoCliente estado;
    private Date fechaCreacion;
    private Date fechaModificacion;
}
