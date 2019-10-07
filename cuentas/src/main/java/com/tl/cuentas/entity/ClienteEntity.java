package com.tl.cuentas.entity;

import com.tl.cuentas.model.Parametro;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "clientes",
       uniqueConstraints = @UniqueConstraint(name = "identificacion_ukey", columnNames = {"tipoId", "numeroId"}))
@DynamicUpdate
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClienteEntity {

    @Id
    @GeneratedValue (generator="numeroCliente", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "clientes_skey", name = "numeroCliente", initialValue = 1001, allocationSize = 1)
    @Column(insertable = false, updatable = false)
    private Long numeroCliente;

    @NaturalId
    @NotNull(message = "{clientes.tipoid.notnull}")
    @Column(nullable =  false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Parametro.TipoId tipoId;

    @NaturalId
    @NotNull(message = "{clientes.numeroid.notnull}")
    @Size(min = 5, max = 20, message = "{clientes.numeroid.size}")
    @Pattern(regexp = "^[0-9]+$", message = "{clientes.numeroid.only-numbers}")
    @Column(nullable = false, updatable = false)
    private String numeroId;

    @NotNull(message = "{clientes.nombres.notnull}")
    @Size(min = 2, max = 50, message = "{clientes.nombres.size}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{clientes.nombres.only-chars-and-spaces}")
    @Column(nullable = false)
    private String nombres;

    @NotNull(message = "{clientes.apellidos.notnull}")
    @Size(min = 2, max = 50, message = "{clientes.apellidos.size}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{clientes.apellidos.only-chars-and-spaces}")
    @Column(nullable = false)
    private String apellidos;


    @NotNull(message = "{clientes.estado.notnull}")
    @Enumerated(EnumType.STRING)
    @Column(length = 3, nullable = false)
    private Parametro.EstadoCliente estado;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date fechaCreacion;

    @Column(nullable = false)
    @LastModifiedDate
    private Date fechaModificacion;
}
