package com.tl.cuentas.service;

import com.tl.cuentas.entity.ClienteEntity;
import com.tl.cuentas.entity.CuentaEntity;
import com.tl.cuentas.model.Cliente;
import com.tl.cuentas.model.Cuenta;
import com.tl.cuentas.model.Parametro;
import com.tl.cuentas.model.Transaccion;
import com.tl.cuentas.repository.CuentaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.beans.FeatureDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CuentaService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    public List<Cuenta> consultarCuentasPorCliente(Long numeroCliente) {
        Optional<Cliente> search = clienteService.consultarCliente(numeroCliente);

        if(!search.isPresent()) {
            return Collections.emptyList();
        }

        CuentaEntity filter = new CuentaEntity();
        filter.setNumeroCliente(modelMapper.map(search.get(), ClienteEntity.class));

        return cuentaRepository.findAll(Example.of(filter)).stream()
                .filter(a -> !a.getEstado().equals(Parametro.EstadoCuenta.INA))
                .map(a -> modelMapper.map(a, Cuenta.class))
                .sorted(Comparator.comparing(Cuenta::getNumeroCuenta))
                .collect(Collectors.toList());
    }

    public Optional<Cuenta> consultarCuenta(Long numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .filter(a -> !a.getEstado().equals(Parametro.EstadoCuenta.INA))
                .map(a -> modelMapper.map(a, Cuenta.class));
    }

    public Optional<Cuenta> crearCuenta(Long numeroCliente) {
        Optional<Cliente> search = clienteService.consultarCliente(numeroCliente);

        if(!search.isPresent()) {
            return Optional.empty();
        }

        CuentaEntity resultEntity = new CuentaEntity();
        resultEntity.setNumeroCliente(modelMapper.map(search.get(), ClienteEntity.class));
        resultEntity.setSaldoDisponible(0L);
        resultEntity.setConsumosPendientesPago(0L);
        resultEntity.setEstado(Parametro.EstadoCuenta.ACT);

        validateCuentaEntity(resultEntity);
        resultEntity = cuentaRepository.save(resultEntity);

        return Optional.of(modelMapper.map(resultEntity, Cuenta.class));
    }

    public Optional<Cuenta> actualizarCuenta(Long numeroCuenta, Cuenta cuenta) {
        Optional<Cuenta> search = consultarCuenta(numeroCuenta);

        if(!search.isPresent()) {
            return Optional.empty();
        }

        if (cuenta.getEstado() != null && cuenta.getEstado().equals(Parametro.EstadoCuenta.INA)) {
            String message = messageSource.getMessage("upd.cuentas.exc-operation-not-allowed", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        BeanUtils.copyProperties(cuenta, search.get(), getNullProperties(cuenta));
        CuentaEntity resultEntity = modelMapper.map(search.get(), CuentaEntity.class);
        validateCuentaEntity(resultEntity);
        resultEntity = cuentaRepository.save(resultEntity);

        return Optional.of(modelMapper.map(resultEntity, Cuenta.class));
    }

    public Optional<Cuenta> actualizarCuentaConTransaccion(Long numeroCuenta, Transaccion transaccion) {
        Long nuevoSaldoDisponible;
        Optional<Cuenta> search = consultarCuenta(numeroCuenta);

        if(!search.isPresent()) {
            return Optional.empty();
        }

        Cuenta resource = search.get();

        switch(transaccion.getTipo()) {
            case CON:
                nuevoSaldoDisponible = resource.getSaldoDisponible() - transaccion.getMonto();

                if(resource.getConsumosPendientesPago().equals(Parametro.MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO)) {
                    String message = messageSource.getMessage("upd.cuentas.exc-credit-exceeded",
                                                              new Object[] { Parametro.MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO },
                                                              Locale.getDefault());
                    throw new IllegalArgumentException(message);
                }

                if(nuevoSaldoDisponible < 0) {
                    resource.setConsumosPendientesPago(resource.getConsumosPendientesPago() + 1L);
                }

                resource.setSaldoDisponible(nuevoSaldoDisponible);
            break;
            case REC:
                nuevoSaldoDisponible = resource.getSaldoDisponible() + transaccion.getMonto();

                if(nuevoSaldoDisponible < 0) {
                    String message = messageSource.getMessage("upd.cuentas.exc-insufficient-recharge", null, Locale.getDefault());
                    throw new IllegalArgumentException(message);
                }

                if(nuevoSaldoDisponible > Parametro.MAXIMO_SALDO_DISPONIBLE) {
                    String message = messageSource.getMessage("upd.cuentas.exc-balance-exceeded",
                                                              new Object[] { Parametro.MAXIMO_SALDO_DISPONIBLE },
                                                              Locale.getDefault());
                    throw new IllegalArgumentException(message);
                }

                resource.setConsumosPendientesPago(0L);
                resource.setSaldoDisponible(nuevoSaldoDisponible);
            break;
        }

        CuentaEntity resultEntity = modelMapper.map(resource, CuentaEntity.class);
        validateCuentaEntity(resultEntity);
        resultEntity = cuentaRepository.save(resultEntity);

        return Optional.of(modelMapper.map(resultEntity, Cuenta.class));
    }

    public boolean eliminarCuenta(Long numeroCuenta) {
        Optional<Cuenta> search = consultarCuenta(numeroCuenta);

        if(!search.isPresent()) {
            return false;
        }

        Cuenta resource = search.get();

        if(resource.getConsumosPendientesPago() > 0) {
            String message = messageSource.getMessage("del.cuentas.exc-outstanding-credit", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        resource.setEstado(Parametro.EstadoCuenta.INA);
        CuentaEntity resultEntity = modelMapper.map(resource, CuentaEntity.class);
        cuentaRepository.save(resultEntity);

        return true;
    }

    private String[] getNullProperties(Cuenta cuenta) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(cuenta);

        return Stream.of(beanWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(a -> beanWrapper.getPropertyValue(a) == null)
                .toArray(String[]::new);
    }

    private void validateCuentaEntity(CuentaEntity cuentaEntity) {
        Set<ConstraintViolation<CuentaEntity>> constraintViolations = validator.validate(cuentaEntity);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
