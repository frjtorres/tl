package com.tl.cuentas.unit;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.tl.cuentas.component.TestMapper;
import com.tl.cuentas.model.Cuenta;
import com.tl.cuentas.model.Parametro;
import com.tl.cuentas.model.Transaccion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CuentaUnit {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestMapper testMapper;

    /*
     * get: omitir la representación de cuentas eliminados.
     */
    @Test
    public void consultarCuentasEliminadas() throws Exception {
        // given
        long numeroCliente = 1006L;
        long numeroCuenta = 1001002L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/clientes/" + numeroCliente + "/cuentas")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        List<Cuenta> output = testMapper.mapManyFromJson(mvcResult.getResponse().getContentAsString(), Cuenta.class);

        // then
        assertEquals(0, output.stream()
                .filter(a -> a.getNumeroCuenta() == numeroCuenta)
                .count());
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * get: omitir la representación de cuenta eliminada.
     */
    @Test
    public void consultarCuentaEliminada() throws Exception {
        // given
        long numeroCuenta = 1001002L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cuentas/" + numeroCuenta)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * patch: omitir la eliminación de cuentas en esta operación.
     */
    @Test
    public void actualizarCuentasParaEliminarlas() throws Exception {
        //given
        long numeroCuenta = 1001003L;
        Cuenta input = new Cuenta();
        input.setEstado(Parametro.EstadoCuenta.INA);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/cuentas/" + numeroCuenta)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * patch: omitir recargas en cuentas si el monto superta el máximo
     *        saldo disponible permitido.
     */
    @Test
    public void actualizarCuentasMaximoSaldoPermitido() throws Exception {
        //given
        long numeroCuenta = 1001004L;
        Transaccion input = new Transaccion();
        input.setTipo(Parametro.TipoTransaccion.REC);
        input.setMonto(Parametro.MAXIMO_SALDO_DISPONIBLE + 1);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/cuentas/" + numeroCuenta + "/transacciones")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * patch: omitir recargas en cuentas si el monto no es suficiente para
     *        cubrir los consumos pendientes por pago.
     */
    @Test
    public void actualizarCuentasRecargasInsuficientes() throws Exception {
        //given
        long numeroCuenta = 1001005L;
        Transaccion input = new Transaccion();
        input.setTipo(Parametro.TipoTransaccion.REC);
        input.setMonto(50L);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/cuentas/" + numeroCuenta + "/transacciones")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * patch: omitir los consumos en cuentas si el número de consumos
     *        pendientes por pago superan el máximo de crédito permitido.
     */
    @Test
    public void actualizarCuentasMaximoCreditoPermitido() throws Exception {
        //given
        long numeroCuenta = 1001006L;
        Transaccion input = new Transaccion();
        input.setTipo(Parametro.TipoTransaccion.CON);
        input.setMonto(2400L);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/cuentas/" + numeroCuenta + "/transacciones")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    /*
     * delete: omitir la eliminacion de cuentas con consumos pendientes por pago.
     */
    @Test
    public void eliminarCuentasConConsumosPendientesPago() throws Exception {
        //given
        long numeroCuenta = 1001007L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/cuentas/" + numeroCuenta)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }
}
