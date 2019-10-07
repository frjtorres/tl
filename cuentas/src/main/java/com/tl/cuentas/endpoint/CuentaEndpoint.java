package com.tl.cuentas.endpoint;

import com.tl.cuentas.model.Cuenta;
import com.tl.cuentas.model.Transaccion;
import com.tl.cuentas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
public class CuentaEndpoint {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/clientes/{numeroCliente}/cuentas")
    public ResponseEntity<List<Cuenta>> consultarCuentasPorCliente(@PathVariable("numeroCliente") Long numeroCliente) {
        List<Cuenta> resources = cuentaService.consultarCuentasPorCliente(numeroCliente);

        if(resources == null) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @GetMapping("/cuentas/{numeroCuenta}")
    public ResponseEntity<Cuenta> consultarCuenta(@PathVariable("numeroCuenta") Long numeroCuenta) {
        Cuenta resource = cuentaService.consultarCuenta(numeroCuenta);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping("/clientes/{numeroCliente}/cuentas")
    public ResponseEntity<?> crearCuenta(@PathVariable("numeroCliente") Long numeroCliente) {
        Cuenta resource = cuentaService.crearCuenta(numeroCliente);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PatchMapping("/cuentas/{numeroCuenta}")
    public ResponseEntity<Cuenta> actualizarCuenta(@PathVariable Long numeroCuenta,
                                                   @RequestBody Cuenta cuenta) {
        Cuenta resource = cuentaService.actualizarCuenta(numeroCuenta, cuenta);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PatchMapping("/cuentas/{numeroCuenta}/transacciones")
    public ResponseEntity<Cuenta> actualizarCuentaConTransaccion(@PathVariable Long numeroCuenta,
                                                                 @RequestBody Transaccion transaccion) {
        Cuenta resource = cuentaService.actualizarCuentaConTransaccion(numeroCuenta, transaccion);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @DeleteMapping("/cuentas/{numeroCuenta}")
    public ResponseEntity eliminarCuenta(@PathVariable Long numeroCuenta) {
        if(!cuentaService.eliminarCuenta(numeroCuenta)) {
            String httpMessage = messageSource.getMessage("all.cuentas.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
