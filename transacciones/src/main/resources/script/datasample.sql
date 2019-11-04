/* ====== Transaccion samples ====== */
-- 1001001001
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'REC', 1001001, 5000, '2019-03-25 12:11:00');
-- 1001001002
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'REC', 1001001, 20000, '2019-03-25 12:12:00');
-- 1001001003
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001001, 2200, '2019-03-25 12:13:00');
-- 1001001004
INSERT INTO transacciones (numero_transaccion, tipo, numero_cuenta, monto, fecha_creacion)
VALUES (transacciones_skey.nextval, 'CON', 1001001, 0, '2019-03-25 12:14:00');

/* ====== Recarga samples ====== */
-- 1001001001
INSERT INTO recargas (numero_transaccion)
VALUES (1001001001);
-- 1001001002
INSERT INTO recargas (numero_transaccion)
VALUES (1001001002);

/* ====== Consumo samples ====== */
-- 1001001003
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001003, 'ENT', 'TRO');
-- 1001001004
INSERT INTO consumos (numero_transaccion, modalidad, servicio)
VALUES (1001001004, 'TRA', 'TRO');
