package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: 1- omitir recargas a cuentas no activas. " +
                                     "2- omitir consumos a cuentas no activas."

        request {
            url ("/cuentas/1001008")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body (
                [
                    numeroCuenta: "1001008",
                    numeroCliente: "1007",
                    saldoDisponible: "10000",
                    consumosPendientesPago: "0",
                    estado: "BLO"
                ]
            )
        }
    }
]