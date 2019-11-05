package com.tl.transacciones.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "transacciones")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", length = 3, discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TransaccionEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="numeroTransaccion")
    @SequenceGenerator(sequenceName = "transacciones_skey",
                       name = "numeroTransaccion",
                       initialValue = 1001001001,
                       allocationSize = 1)
    @Column(insertable = false, updatable = false)
    private Long numeroTransaccion;

    @Column(insertable = false, updatable = false)
    private String tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "numeroCuenta",
                referencedColumnName = "numeroCuenta",
                foreignKey = @ForeignKey(name = "cuentas_fkey"),
                updatable = false)
    private CuentaEntity numeroCuenta;

    @NotNull(message = "{transaccion.monto.notnull}")
    @Digits(integer = 6, fraction = 0, message = "{transaccion.monto.digits}")
    @Column(nullable = false)
    private Long monto;


    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date fechaCreacion;
}
