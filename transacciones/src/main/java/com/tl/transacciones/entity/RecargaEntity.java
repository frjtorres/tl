package com.tl.transacciones.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "recargas")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "recargas_fkey"))
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "REC")
public class RecargaEntity extends TransaccionEntity {}
