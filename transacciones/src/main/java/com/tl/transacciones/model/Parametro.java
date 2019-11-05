package com.tl.transacciones.model;

public class Parametro {
    public static final long MAXIMO_MONTO_CUENTA = 100_000L;
    public static final Long MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO = 2L;
    public static final long MAXIMO_TIEMPO_TRANSBORDO = 95L;
    public static final long MAXIMO_NUMERO_TRANSBORDOS = 2L;
    public static final long COSTO_SERVICIO_TRONCAL = 2_400L;
    public static final long COSTO_SERVICIO_ZONAL = 2_200L;
    public static final long COSTO_TRASBORDO_DIFERENTE_SERVICIO = 200L;
    public static final long COSTO_TRASBORDO_MISMO_SERVICIO = 0L;

    public enum EstadoCuenta { ACT, BLO, ELI }
    public enum ModalidadConsumo { ENT, TRA }
    public enum ServicioConsumo { TRO, ZON }
}


