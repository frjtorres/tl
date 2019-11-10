package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: omitir recargas cuando el saldo disponible supere el limite."

        request {
            url ("/cuentas/1001010")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body (
                [
                    numeroCuenta: "1001010",
                    numeroCliente: "1007",
                    saldoDisponible: "300000",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    }
]