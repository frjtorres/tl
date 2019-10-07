package com.tl.cuentas.endpoint;

import com.tl.cuentas.model.Cliente;
import com.tl.cuentas.model.Parametro;
import com.tl.cuentas.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/clientes")
public class ClienteEndpoint {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Cliente>> consultarClientes(@RequestParam(value = "tipoId", required = false)
                                                           String tipoId,
                                                           @RequestParam(value = "numeroId", required = false)
                                                           String numeroId) {
        Parametro.TipoId enumTipoId = (tipoId != null)? Parametro.TipoId.valueOf(tipoId): null;
        List<Cliente> resources = clienteService.consultarClientesPorId(enumTipoId, numeroId);

        if(resources == null) {
            String httpMessage = messageSource.getMessage("all.clientes.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @GetMapping("/{numeroCliente}")
    public ResponseEntity<Cliente> consultarCliente(@PathVariable("numeroCliente") Long numeroCliente) {
        Cliente resource = clienteService.consultarCliente(numeroCliente);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.clientes.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente resource = clienteService.crearCliente(cliente);

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PatchMapping("/{numeroCliente}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long numeroCliente,
                                                    @RequestBody Cliente cliente) {
        Cliente resource = clienteService.actualizarCliente(numeroCliente, cliente);

        if(resource == null) {
            String httpMessage = messageSource.getMessage("all.clientes.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @DeleteMapping("/{numeroCliente}")
    public ResponseEntity eliminarCliente(@PathVariable Long numeroCliente) {
        if(!clienteService.eliminarCliente(numeroCliente)) {
            String httpMessage = messageSource.getMessage("all.clientes.not-found", null, Locale.getDefault());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpMessage);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
