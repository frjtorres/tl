package com.tl.transacciones.entity;

import com.tl.transacciones.model.Parametro;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "consumos")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "consumos_fkey"))
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "CON")
public class ConsumoEntity extends TransaccionEntity {

    @Column(length = 3, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Parametro.ModalidadConsumo modalidad;

    @NotNull(message = "{consumo.servicio.notnull}")
    @Column(length = 3, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Parametro.ServicioConsumo servicio;
}
