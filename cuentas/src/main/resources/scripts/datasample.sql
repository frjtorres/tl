/* ====== Clientes samples ====== */
-- 1001
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010123000', 'Ana', 'Suarez', 'ACT', '2018-01-01 01:12:43', '2018-01-05 23:01:47');
-- 1002
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010456000', 'Antonio', 'Ariza', 'ACT', '2019-03-02 09:56:38', '2019-04-05 21:55:11');
-- 1003
INSERT INTO clientes (numero_cliente, tipo_id, numero_id, nombres, apellidos, estado, fecha_creacion, fecha_modificacion)
VALUES(clientes_skey.nextval, 'CC', '1010789000', 'Andrea', 'Rodriguez', 'ACT', '2019-06-15 14:44:59', '2019-06-16 17:32:29');

/* ====== Cuentas samples ====== */
-- 1001001
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1001', 10000, 0, 'ACT', '2019-01-02 01:13:43', '2019-01-02 01:13:43');
-- 1001002
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1002', 0, 0, 'ACT', '2018-03-02 09:56:45', '2019-03-02 09:56:45');
-- 1001003
INSERT INTO cuentas (numero_cuenta, numero_cliente, saldo_disponible, consumos_pendientes_pago, estado, fecha_creacion, fecha_modificacion)
VALUES(cuentas_skey.nextval, '1003', -2400, 1, 'ACT', '2018-06-15 17:44:59', '2019-06-15 17:44:59');
