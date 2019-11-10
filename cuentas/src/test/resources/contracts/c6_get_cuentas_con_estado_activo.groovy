package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo de servicio troncal."

        request {
            url ("/cuentas/1001011")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001011",
                    numeroCliente: "1007",
                    saldoDisponible: "25000",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo de servicio zonal."

        request {
            url ("/cuentas/1001012")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001012",
                    numeroCliente: "1007",
                    saldoDisponible: "21000",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo con trasbordo a diferente servicio."

        request {
            url ("/cuentas/1001013")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001013",
                    numeroCliente: "1007",
                    saldoDisponible: "-1000",
                    consumosPendientesPago: "1",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: permitir la creación de un consumo con trasbordo al mismo servicio."

        request {
            url ("/cuentas/1001014")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001014",
                    numeroCliente: "1007",
                    saldoDisponible: "-1000",
                    consumosPendientesPago: "1",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "casos de prueba: aplicar costo de entrada cuando el viaje supere el maximo tiempo " +
                                     "permitido para trasbordar."

        request {
            url ("/cuentas/1001015")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001015",
                    numeroCliente: "1007",
                    saldoDisponible: "-1500",
                    consumosPendientesPago: "1",
                    estado: "ACT"
                ]
            )
        }
    },
    Contract.make {
        description "caso de pruebas: aplicar costo de entrada cuenta el viaje supere el numero maximo " +
                                     "de trasbordos."

        request {
            url ("/cuentas/1001016")
            method GET()
        }

        response {
            status OK()
            headers { contentType applicationJson() }
            body(
                [
                    numeroCuenta: "1001016",
                    numeroCliente: "1007",
                    saldoDisponible: "8500",
                    consumosPendientesPago: "0",
                    estado: "ACT"
                ]
            )
        }
    }
]
