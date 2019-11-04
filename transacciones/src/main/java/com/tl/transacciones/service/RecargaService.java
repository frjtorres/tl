package com.tl.transacciones.service;

import com.tl.transacciones.model.Cuenta;
import com.tl.transacciones.model.Parametro;
import com.tl.transacciones.entity.RecargaEntity;
import com.tl.transacciones.model.Recarga;
import com.tl.transacciones.repository.RecargaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import java.util.Collections;
import java.util.Locale;

@Service
public class RecargaService {

    @Autowired
    private RecargaRepository recargaRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    public Recarga crearRecarga(Cuenta cuenta, Recarga recarga) {
        if(!cuenta.getEstado().equals(Parametro.EstadoCuenta.ACT)) {
            String message = messageSource.getMessage("cre.recarga.exc-active-accounts", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        if(recarga.getMonto() <= 0) {
            String message = messageSource.getMessage("cre.recarga.exc-min-recharge", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        if((cuenta.getSaldoDisponible() + recarga.getMonto()) < 0) {
            String message = messageSource.getMessage("cre.recarga.exc-insufficient-recharge", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        if((cuenta.getSaldoDisponible() + recarga.getMonto()) > Parametro.MAXIMO_MONTO_CUENTA) {
            String message = messageSource.getMessage("cre.recarga.exc-balance-exceeded",
                                                      new Object[] { Parametro.MAXIMO_MONTO_CUENTA },
                                                      Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        recarga.setNumeroCuenta(new Cuenta(cuenta.getNumeroCuenta()));
        RecargaEntity recargaEntity = modelMapper.map(recarga, RecargaEntity.class);
        recargaEntity = recargaRepository.save(recargaEntity);
        recarga = modelMapper.map(recargaEntity, Recarga.class);
        recarga.setTipo(recargaEntity.getClass().getAnnotation(DiscriminatorValue.class).value());

        return recarga;
    }
}
