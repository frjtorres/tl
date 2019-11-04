package com.tl.transacciones.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
public class CuentaEntity {

    @Id
    private Long numeroCuenta;
}
