package com.tl.cuentas.service;

import com.tl.cuentas.entity.ClienteEntity;
import com.tl.cuentas.model.Cliente;
import com.tl.cuentas.model.Cuenta;
import com.tl.cuentas.model.Parametro;
import com.tl.cuentas.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.beans.FeatureDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validator validator;

    public List<Cliente> consultarClientesPorId(Parametro.TipoId tipoId, String numeroId) {
        ClienteEntity filter = new ClienteEntity();
        filter.setTipoId(tipoId);
        filter.setNumeroId(numeroId);

        return clienteRepository.findAll(Example.of(filter)).stream()
                .filter(a -> a.getEstado().equals(Parametro.EstadoCliente.ACT))
                .map(a -> modelMapper.map(a, Cliente.class))
                .sorted(Comparator.comparing(Cliente::getNumeroCliente))
                .collect(Collectors.toList());
    }

    public Cliente consultarCliente(Long numeroCliente) {
        return clienteRepository.findById(numeroCliente)
                .filter(a -> a.getEstado().equals(Parametro.EstadoCliente.ACT))
                .map(a -> modelMapper.map(a, Cliente.class))
                .orElse(null);
    }

    public Cliente crearCliente(Cliente cliente) {
        ClienteEntity filter = new ClienteEntity();
        filter.setTipoId(cliente.getTipoId());
        filter.setNumeroId(cliente.getNumeroId());

        ClienteEntity answer = clienteRepository.findOne(Example.of(filter)).orElse(null);

        if(answer != null && answer.getEstado().equals(Parametro.EstadoCliente.ACT)) {
            String message = messageSource.getMessage("cre.clientes.exc-already-exists", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        if(answer != null && answer.getEstado().equals(Parametro.EstadoCliente.INA)) {
            cliente.setNumeroCliente(answer.getNumeroCliente());
            cliente.setFechaCreacion(answer.getFechaCreacion());
        }

        cliente.setEstado(Parametro.EstadoCliente.ACT);
        ClienteEntity resultEntity = modelMapper.map(cliente, ClienteEntity.class);
        validateClienteEntity(resultEntity);
        resultEntity = clienteRepository.save(resultEntity);

        return modelMapper.map(resultEntity, Cliente.class);
    }

    public Cliente actualizarCliente(Long numeroCliente, Cliente cliente) {
        Cliente resource = consultarCliente(numeroCliente);

        if(resource == null) {
            return null;
        }

        cliente.setNumeroCliente(null);
        cliente.setTipoId(null);
        cliente.setNumeroId(null);
        BeanUtils.copyProperties(cliente, resource, getNullProperties(cliente));
        ClienteEntity resultEntity = modelMapper.map(resource, ClienteEntity.class);
        validateClienteEntity(resultEntity);
        resultEntity = clienteRepository.save(resultEntity);

        return modelMapper.map(resultEntity, Cliente.class);
    }

    public boolean eliminarCliente(Long numeroCliente) {
        Cliente resource = consultarCliente(numeroCliente);

        if(resource == null) {
            return false;
        }

        List<Cuenta> cuentas = cuentaService.consultarCuentasPorCliente(numeroCliente);

        if(!cuentas.isEmpty()) {
            String message = messageSource.getMessage("del.clientes.exc-active-accounts", null, Locale.getDefault());
            throw new IllegalArgumentException(message);
        }

        resource.setEstado(Parametro.EstadoCliente.INA);
        ClienteEntity resultEntity = modelMapper.map(resource, ClienteEntity.class);
        clienteRepository.save(resultEntity);

        return true;
    }

    private String[] getNullProperties(Cliente cliente) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(cliente);

        return Stream.of(beanWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(a -> beanWrapper.getPropertyValue(a) == null)
                .toArray(String[]::new);
    }

    private void validateClienteEntity(ClienteEntity clienteEntity) {
        Set<ConstraintViolation<ClienteEntity>> constraintViolations = validator.validate(clienteEntity);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
