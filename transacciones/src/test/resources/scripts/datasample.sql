/* ====== integration samples ====== */
/* omitir_recargas_en_cuentas_no_activas */
/* omitir_consumos_en_cuentas_no_activas */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001008);

/* omitir_recargas_cuando_el_monto_no_cubre_los_consumos_pendientes_de_pago */
/* omitir_consumos_cuando_los_consumos_pendientes_de_pago_superen_el_limite */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001009);

/* omitir_recargas_cuando_el_saldo_disponible_supere_el_limite */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001010);

/* permitir_creacion_de_consumo_de_servicio_troncal */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001011);
-- 1001001001
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001011, 2400, dateadd(MINUTE, -100, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001001, 'ENT', 'TRO');

/* permitir_creacion_de_consumo_de_servicio_zonal */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001012);
-- 1001001002
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001012, 2200, dateadd(MINUTE, -100, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001002, 'ENT', 'ZON');

/* permitir_creacion_de_trasbordo_a_diferente_servicio */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001013);
-- 1001001003
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001013, 2200, dateadd(MINUTE, -94, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001003, 'ENT', 'ZON');
-- 1001001004
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001013, 0, dateadd(MINUTE, -90, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001004, 'TRA', 'ZON');

/* permitir_creacion_de_trasbordo_al_mismo_servicio */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001014);
-- 1001001005
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001014, 2200, dateadd(MINUTE, -94, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001005, 'ENT', 'ZON');

/* aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_tiempo_para_trasbordar */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001015);
-- 1001001006
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001015, 2200, dateadd(MINUTE, -96, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001006, 'ENT', 'ZON');

/* aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_numero_de_trasbordos */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001016);
-- 1001001007
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001016, 2400, dateadd(MINUTE, -94, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001007, 'ENT', 'TRO');
-- 1001001008
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001016, 200, dateadd(MINUTE, -88, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001008, 'TRA', 'ZON');
-- 1001001009
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001016, 0, dateadd(MINUTE, -71, current_timestamp));
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001009, 'TRA', 'ZON');

/* omitir_consulta_de_transacciones_para_cuenta_eliminada */
/* omitir_consulta_de_una_transaccion_para_cuenta_eliminada */
INSERT INTO cuentas (numero_cuenta)
VALUES (1001017);
-- 1001001010
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'REC', 1001017, 2400, '2019-03-25 12:12:00');
INSERT INTO recargas (numero_transaccion)
VALUES (1001001010);
-- 1001001011
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001017, 2400, '2019-03-25 12:12:00');
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001011, 'ENT', 'TRO');
