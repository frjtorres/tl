package com.tl.cuentas.unit;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
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

    @Test
    public void omitir_consulta_de_cuentas_eliminadas() throws Exception {
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

    @Test
    public void omitir_consulta_de_cuenta_eliminada() throws Exception {
        // given
        long numeroCuenta = 1001002L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cuentas/" + numeroCuenta)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_eliminacion_de_cuentas_al_actualizarlas() throws Exception {
        // given
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

    @Test
    public void omitir_recargas_si_el_nuevo_saldo_disponible_supera_el_limite() throws Exception {
        // given
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

    @Test
    public void omitir_recargas_si_el_monto_es_insuficiente_para_cubrir_los_consumos_pendientes_por_pago() throws Exception {
        // given
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

    @Test
    public void omitir_consumos_si_el_numero_de_consumos_pendientes_por_pago_supera_el_limite() throws Exception {
        // given
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

    @Test
    public void omitir_aumento_de_consumos_pendientes_pago_si_monto_de_la_transaccion_es_cero() throws Exception {
        // given
        long numeroCuenta = 1001007L;
        long consumosPendientesPago = 1L;
        Transaccion input = new Transaccion();
        input.setTipo(Parametro.TipoTransaccion.CON);
        input.setMonto(0L);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/cuentas/" + numeroCuenta + "/transacciones")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['consumosPendientesPago']").isEqualTo(consumosPendientesPago);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_eliminacion_de_cuentas_con_consumos_pendientes_por_pago() throws Exception {
        // given
        long numeroCuenta = 1001008L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/cuentas/" + numeroCuenta)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }
}
