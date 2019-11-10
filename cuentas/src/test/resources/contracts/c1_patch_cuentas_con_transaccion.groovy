package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo de servicio troncal."

        request {
            url ("/cuentas/1001011/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "2400"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001011",
                    numeroCliente: "1007",
                    saldoDisponible: "22600",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo de servicio zonal."

        request {
            url ("/cuentas/1001012/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "2200"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001012",
                    numeroCliente: "1007",
                    saldoDisponible: "18800",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo con trasbordo a diferente servicio."

        request {
            url ("/cuentas/1001013/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "200"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001013",
                    numeroCliente: "1007",
                    saldoDisponible: "-1200",
                    consumosPendientesPago: "2",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo con trasbordo al mismo servicio."

        request {
            url ("/cuentas/1001014/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "0"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001014",
                    numeroCliente: "1007",
                    saldoDisponible: "-1000",
                    consumosPendientesPago: "2",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: aplicar costo de entrada cuando el viaje supere el maximo tiempo " +
                                     "permitido para trasbordar."

        request {
            url ("/cuentas/1001015/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "2200"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001015",
                    numeroCliente: "1007",
                    saldoDisponible: "-3700",
                    consumosPendientesPago: "2",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: aplicar costo de entrada cuenta el viaje supere el numero maximo " +
                                     "de trasbordos. "
        request {
            url ("/cuentas/1001016/transacciones")
            method PATCH()
            headers { contentType applicationJson() }
            body(
                [
                    tipo: "CON",
                    monto: "2400"
                ]
            )
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001016",
                    numeroCliente: "1007",
                    saldoDisponible: "6100",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    }
]
