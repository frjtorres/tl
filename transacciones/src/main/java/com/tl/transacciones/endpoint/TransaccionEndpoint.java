package com.tl.transacciones.endpoint;

import com.tl.transacciones.model.Consumo;
import com.tl.transacciones.model.Cuenta;
import com.tl.transacciones.model.Recarga;
import com.tl.transacciones.model.Transaccion;
import com.tl.transacciones.service.ConsumoService;
import com.tl.transacciones.service.RecargaService;
import com.tl.transacciones.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collections;
import java.util.Locale;

@RestController
public class TransaccionEndpoint {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private RecargaService recargaService;

    @Autowired
    private ConsumoService consumoService;

    @Autowired
    private MessageSource messageSource;

    @Value("${com.tl.clientes.url}")
    private String clientesUrl;

    @GetMapping("/cuentas/{numeroCuenta}/transacciones")
    public ResponseEntity<Cuenta> consultarTransaccionesPorCuenta(@PathVariable("numeroCuenta") Long numeroCuenta) {
        Cuenta cuenta = restTemplate.getForEntity(clientesUrl + "/cuentas/" + numeroCuenta, Cuenta.class).getBody();
        Cuenta resource = transaccionService.consultarTransaccionesPorCuenta(cuenta);

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/transacciones/{numeroTransaccion}")
    public ResponseEntity<Transaccion> consultarTransaccion(@PathVariable("numeroTransaccion") Long numeroTransaccion) {
        Transaccion resource = transaccionService.consultarTransaccion(numeroTransaccion);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.transacciones.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping("/cuentas/{numeroCuenta}/recargas")
    public ResponseEntity<Cuenta> crearRecarga(@PathVariable("numeroCuenta") Long numeroCuenta,
                                               @RequestBody Recarga recarga) {
        Cuenta cuenta = restTemplate.getForEntity(clientesUrl + "/cuentas/" + numeroCuenta, Cuenta.class).getBody();
        Recarga resource = recargaService.crearRecarga(cuenta, recarga);
        cuenta = restTemplate.exchange(RequestEntity.patch(URI.create(clientesUrl + "/cuentas/" + numeroCuenta + "/transacciones"))
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(resource), Cuenta.class).getBody();
        cuenta.setTransacciones(Collections.singletonList(resource));

        return ResponseEntity.status(HttpStatus.OK).body(cuenta);
    }

    @PostMapping("/cuentas/{numeroCuenta}/consumos")
    public ResponseEntity<Cuenta> crearConsumo(@PathVariable("numeroCuenta") Long numeroCuenta,
                                               @RequestBody Consumo consumo) {
        Cuenta cuenta = restTemplate.getForEntity(clientesUrl + "/cuentas/" + numeroCuenta , Cuenta.class).getBody();
        Consumo resource = consumoService.crearConsumo(cuenta, consumo);
        cuenta = restTemplate.exchange(RequestEntity.patch(URI.create(clientesUrl + "/cuentas/" + numeroCuenta + "/transacciones"))
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(resource), Cuenta.class).getBody();
        cuenta.setTransacciones(Collections.singletonList(resource));

        return ResponseEntity.status(HttpStatus.OK).body(cuenta);
    }
}
