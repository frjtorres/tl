/* ====== Cliente unit samples ====== */
/* consultarCliente(s)Eliminado(s) */
-- 1001
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010123000', 'Ana', 'Rodriguez', 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* crearClientesIdDuplicadas*/
-- 1002
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010234000', 'Andres', 'Suarez', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* crearClientesEliminados */
-- 1003
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010345000', 'Andrea', 'Aristegui', 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* actualizarClientesDatosNoPermitidos */
-- 1004
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010456000', 'Antonio', 'Ariza', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* eliminarClientesCuentasActivas */
-- 1005
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010567000', 'Antonio', 'Ariza', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');
-- 1001001
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1005', 10000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');


/* ====== Cuenta unit samples ====== */
-- 1006
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010678000', 'Antonio', 'Ariza', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* consultarCuenta(s)Eliminada(s) */
-- 1001002
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 10000, 0, 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* actualizarCuentasParaEliminarlas */
-- 1001003
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 10000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* actualizarCuentasMaximoSaldoPermitido */
-- 1001004
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 0, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* actualizarCuentasRecargasInsuficientes */
-- 1001005
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -2400, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* actualizarCuentasMaximoCreditoPermitido*/
-- 1001006
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -2700, 2, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* eliminarCuentasConConsumosPendientesPago */
-- 1001007
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -1900, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');
