/* ====== Cliente unit samples ====== */
/* omitir_consulta_de_clientes_eliminados */
/* omitir_consulta_de_cliente_eliminado */
-- 1001
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010123000', 'Ana', 'Rodriguez', 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_creacion_de_clientes_con_id_duplicada*/
-- 1002
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010234000', 'Andres', 'Suarez', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_creacion_de_clientes_eliminados_anteriormente */
-- 1003
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010345000', 'Andrea', 'Aristegui', 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* permitir_solamente_actualizacion_de_nombres_y_apellidos_del_cliente */
-- 1004
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010456000', 'Antonio', 'Ariza', 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_eliminacion_de_clientes_con_cuentas_activas */
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

/* omitir_consulta_de_cuentas_eliminadas */
/* omitir_consulta_de_cuenta_eliminada */
-- 1001002
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 10000, 0, 'INA', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_eliminacion_de_cuentas_al_actualizarlas */
-- 1001003
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 10000, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_recargas_si_el_nuevo_saldo_disponible_supera_el_limite */
-- 1001004
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', 0, 0, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_recargas_si_el_monto_es_insuficiente_para_cubrir_los_consumos_pendientes_por_pago */
-- 1001005
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -2400, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_consumos_si_el_numero_de_consumos_pendientes_por_pago_supera_el_limite*/
-- 1001006
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -2700, 2, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_aumento_de_consumos_pendientes_pago_si_monto_de_la_transaccion_es_cero */
-- 1001007
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1006', -1000, 1, 'ACT', '2019-01-01 12:00:00', '2019-01-01 12:00:00');

/* omitir_eliminacion_de_cuentas_con_consumos_pendientes_por_pago */
-- 1001008
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
