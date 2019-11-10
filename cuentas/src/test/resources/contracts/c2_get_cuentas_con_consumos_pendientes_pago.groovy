package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: 1- omitir consumos cuando el numero de consumos pendientes de pago superen el limite. " +
                                     "2- omitir recargas cuando el monto no cubre los consumos pendientes de pago."

        request {
            url ("/cuentas/1001009")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body (
                [
                    numeroCuenta: "1001009",
                    numeroCliente: "1007",
                    saldoDisponible: "-3300",
                    consumosPendientesPago: "2",
                    estado: "ACT"
                ]
            )
        }
    }
]