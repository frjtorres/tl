package com.tl.cuentas.entity;

import com.tl.cuentas.model.Parametro;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "cuentas")
@DynamicUpdate
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CuentaEntity {

    @Id
    @GeneratedValue(generator="numeroCuenta", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "cuentas_skey", name = "numeroCuenta", initialValue = 1001001, allocationSize = 1)
    @Column(insertable = false, updatable = false)
    private Long numeroCuenta;

    @ManyToOne(optional = false)
    @JoinColumn(name="numeroCliente",
                referencedColumnName = "numeroCliente",
                foreignKey = @ForeignKey(name = "clientes_fkey"),
                updatable = false)
    private ClienteEntity numeroCliente;

    @NotNull(message = "{cuentas.saldo-disponible.notnull}")
    @Digits(integer = 6,fraction = 0, message = "{cuentas.saldo-disponible.digits}")
    @Column(nullable = false)
    private Long saldoDisponible;

    @NotNull(message = "{cuentas.consumos-pendientes-pago.notnull}")
    @Digits(integer = 1,fraction = 0, message = "{cuentas.consumos-pendientes-pago.digits}")
    @Column(nullable = false)
    private Long consumosPendientesPago;


    @NotNull(message = "{clientes.estado.notnull}")
    @Enumerated(EnumType.STRING)
    @Column(length = 3, nullable = false)
    private Parametro.EstadoCuenta estado;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date fechaCreacion;

    @Column(nullable = false)
    @LastModifiedDate
    private Date fechaModificacion;
}
