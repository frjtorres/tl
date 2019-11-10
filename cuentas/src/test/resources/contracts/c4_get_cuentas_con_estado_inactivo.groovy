package contracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "casos de prueba: 1- omitir consulta de transacciones para cuenta eliminada. " +
                                     "2- omitir consulta de una transaccion para cuenta eliminada."

        request {
            url ("/cuentas/1001017")
            method GET()
        }

        response {
            status NOT_FOUND()
        }
    }
]