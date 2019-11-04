package com.tl.transacciones.service;

import com.tl.transacciones.entity.ConsumoEntity;
import com.tl.transacciones.entity.CuentaEntity;
import com.tl.transacciones.entity.TransaccionEntity;
import com.tl.transacciones.model.Consumo;
import com.tl.transacciones.model.Cuenta;
import com.tl.transacciones.model.Parametro;
import com.tl.transacciones.repository.ConsumoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import java.time.Duration;
import java.util.*;

@Service
public class ConsumoService {

    @Autowired
    private ConsumoRepository consumoRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    public Consumo crearConsumo(Cuenta cuenta, Consumo consumo) {
        if(!cuenta.getEstado().equals(Parametro.EstadoCuenta.ACT)) {
            String message = messageSource.getMessage("cre.consumo.exc-active-accounts", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        CuentaEntity cuentaEntity = modelMapper.map(cuenta, CuentaEntity.class);
        List<ConsumoEntity> consumosViajeReciente = consumoRepository.consumosViajeReciente(cuentaEntity);

        if(estaEnPeriodoDeTrasbordo(consumosViajeReciente) && !superaCantidadDeTrasbordos(consumosViajeReciente)) {
            consumo.setModalidad(Parametro.ModalidadConsumo.TRA);
            consumo.setMonto(calcularCostoTrasbordo(consumosViajeReciente, consumo));
        } else {
            consumo.setModalidad(Parametro.ModalidadConsumo.ENT);
            consumo.setMonto(calcularCostoViaje(consumo));
        }

        if(cuenta.getConsumosPendientesPago() >= Parametro.MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO) {
            String message = messageSource.getMessage("cre.consumo.exc-credit-exceeded",
                                                      new Object[] { Parametro.MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO },
                                                      Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        consumo.setNumeroCuenta(new Cuenta(cuenta.getNumeroCuenta()));
        ConsumoEntity consumoEntity = modelMapper.map(consumo, ConsumoEntity.class);
        consumoEntity = consumoRepository.save(consumoEntity);
        consumo = modelMapper.map(consumoEntity, Consumo.class);
        consumo.setTipo(consumoEntity.getClass().getAnnotation(DiscriminatorValue.class).value());

        return consumo;
    }

    private boolean estaEnPeriodoDeTrasbordo(List<ConsumoEntity> consumosViajeReciente) {
        if (consumosViajeReciente == null || consumosViajeReciente.isEmpty()) {
            return false;
        }

        Optional<ConsumoEntity> entrada = consumosViajeReciente.stream()
                .filter(a -> a.getModalidad().equals(Parametro.ModalidadConsumo.ENT))
                .findFirst();

        if(!entrada.isPresent()) {
            return false;
        }

        Date fechaCreacionEntrada = entrada.get().getFechaCreacion();
        Date fechaReciente = new Date(System.currentTimeMillis());
        Duration duracionEntradaReciente = Duration.between(fechaCreacionEntrada.toInstant(), fechaReciente.toInstant());

        return (duracionEntradaReciente.toMinutes() <= Parametro.MAXIMO_TIEMPO_TRANSBORDO);
    }

    private boolean superaCantidadDeTrasbordos(List<ConsumoEntity> consumosViajeReciente) {
        long numeroTrasbordosViaje = consumosViajeReciente.stream()
                .filter(a -> a.getModalidad().equals(Parametro.ModalidadConsumo.TRA))
                .count();

        return (numeroTrasbordosViaje >= Parametro.MAXIMO_NUMERO_TRANSBORDOS);
    }

    private long calcularCostoTrasbordo(List<ConsumoEntity> consumosViajeReciente, Consumo consumo) {
        Optional<ConsumoEntity> consumoEntityViajeReciente = consumosViajeReciente.stream()
                .max(Comparator.comparing(TransaccionEntity::getFechaCreacion));

        if(consumoEntityViajeReciente.isPresent() &&
                consumoEntityViajeReciente.get().getServicio().equals(consumo.getServicio())) {
            return Parametro.COSTO_TRASBORDO_MISMO_SERVICIO;
        }

        return Parametro.COSTO_TRASBORDO_DIFERENTE_SERVICIO;
    }

    private long calcularCostoViaje(Consumo consumo) {
        if(consumo.getServicio().equals(Parametro.ServicioConsumo.TRO)) {
            return Parametro.COSTO_SERVICIO_TRONCAL;
        }

        if(consumo.getServicio().equals(Parametro.ServicioConsumo.ZON)) {
            return Parametro.COSTO_SERVICIO_ZONAL;
        }

        String message = messageSource.getMessage("cre.consumo.exc-service-not-found",
                                                  new Object[] { consumo.getServicio() },
                                                  Locale.getDefault());
        throw new IllegalArgumentException(message);
    }
}
