package com.tl.cuentas.model;

public class Parametro {
    public static final Long MAXIMO_SALDO_DISPONIBLE = 339000L;
    public static final Long MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO = 2L;

    public enum TipoId { CC, CE, TI }
    public enum TipoTransaccion { CON, REC }
    public enum EstadoCliente { ACT, INA }
    public enum EstadoCuenta { ACT, BLO, INA }
}
