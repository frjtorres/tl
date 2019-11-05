package com.tl.transacciones.service;

import com.tl.transacciones.entity.CuentaEntity;
import com.tl.transacciones.model.Cuenta;
import com.tl.transacciones.entity.TransaccionEntity;
import com.tl.transacciones.model.Transaccion;
import com.tl.transacciones.repository.TransaccionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Cuenta consultarTransaccionesPorCuenta(Cuenta cuenta) {
        TransaccionEntity filter = new TransaccionEntity();
        filter.setNumeroCuenta(modelMapper.map(cuenta, CuentaEntity.class));
        List<Transaccion> transacciones = transaccionRepository.findAll(Example.of(filter)).stream()
                .map(a -> modelMapper.map(a, Transaccion.class))
                .sorted(Comparator.comparing(Transaccion::getFechaCreacion))
                .collect(Collectors.toList());
        cuenta.setTransacciones(transacciones);

        return cuenta;
    }

    public Optional<Transaccion> consultarTransaccion(Long numeroTransaccion) {
        return transaccionRepository.findById(numeroTransaccion)
                .map(a -> modelMapper.map(a, Transaccion.class));
    }
}
