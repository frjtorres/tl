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


/* ====== contracts samples ====== */
--1007
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES (clientes_skey.nextval, 'TI', '94121258565', 'Diana', 'Hernandez', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_recargas_en_cuentas_no_activas */
/* omitir_consumos_en_cuentas_no_activas */
-- 1001008
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 10000, 0, 'BLO', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_recargas_cuando_el_monto_no_cubre_los_consumos_pendientes_de_pago */
/* omitir_consumos_cuando_los_consumos_pendientes_de_pago_superen_el_limite */
-- 1001009
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', -3300, 2, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_recargas_cuando_el_saldo_disponible_supere_el_limite */
-- 1001010
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 300000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_creacion_de_consumo_de_servicio_troncal */
-- 1001011
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 25000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_creacion_de_consumo_de_servicio_zonal */
-- 1001012
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 21000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_creacion_de_trasbordo_a_diferente_servicio */
-- 1001013
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', -1000, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_creacion_de_trasbordo_al_mismo_servicio */
-- 1001014
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', -1000, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_tiempo_para_trasbordar */
-- 1001015
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', -1500, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_numero_de_trasbordos */
-- 1001016
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 8500, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_consulta_de_transacciones_para_cuenta_eliminada */
/* omitir_consulta_de_una_transaccion_para_cuenta_eliminada */
-- 1001017
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1007', 0, 0, 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');