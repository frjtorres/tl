package com.tl.transacciones.integration;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tl.transacciones.component.TestMapper;
import com.tl.transacciones.endpoint.TransaccionEndpoint;
import com.tl.transacciones.entity.ConsumoEntity;
import com.tl.transacciones.model.Cuenta;
import com.tl.transacciones.model.Parametro;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.tl:cuentas")
public class ConsumoIntegration {

    private String url;

    @Value("${com.tl.cuentas.url}")
    private String dir;

    @StubRunnerPort("cuentas")
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransaccionEndpoint transaccionEndpoint;

    @Before
    public void setUp() {
        url = dir + ":" + port;
        ReflectionTestUtils.setField(transaccionEndpoint, "cuentasUrl", url);
    }

    @Test
    public void omitir_consumos_en_cuentas_no_activas() throws Exception {
        // given
        long numeroCuenta = 1001008L;
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.TRO);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['reasons'][0]").isEqualTo("Los consumos solo estan permitidos para cuentas activas.");
        assertThatJson(parsedJson).field("['status']").isEqualTo(HttpStatus.CONFLICT.value());
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_consumos_cuando_los_consumos_pendientes_de_pago_superen_el_limite() throws Exception {
        // given
        long numeroCuenta = 1001009L;
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.TRO);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['reasons'][0]")
                .isEqualTo("La cuenta no puede tener consumos pendientes de pago mayores a " + Parametro.MAXIMO_NUM_CREDITOS_PENDIENTES_PAGO + ".");
        assertThatJson(parsedJson).field("['status']").isEqualTo(HttpStatus.CONFLICT.value());
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_creacion_de_consumo_de_servicio_troncal() throws Exception {
        // given
        long numeroCuenta = 1001011L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.TRO);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_SERVICIO_TRONCAL));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_SERVICIO_TRONCAL);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.ENT);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.TRO);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_creacion_de_consumo_de_servicio_zonal() throws Exception {
        // given
        long numeroCuenta = 1001012L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.ZON);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_SERVICIO_ZONAL));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_SERVICIO_ZONAL);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.ENT);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.ZON);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_creacion_de_trasbordo_a_diferente_servicio() throws Exception {
        // given
        long numeroCuenta = 1001013L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.TRO);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_TRASBORDO_DIFERENTE_SERVICIO));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_TRASBORDO_DIFERENTE_SERVICIO);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.TRA);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.TRO);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_creacion_de_trasbordo_al_mismo_servicio() throws Exception {
        // given
        long numeroCuenta = 1001014L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.ZON);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_TRASBORDO_MISMO_SERVICIO));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_TRASBORDO_MISMO_SERVICIO);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.TRA);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.ZON);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_tiempo_para_trasbordar() throws Exception {
        // given
        long numeroCuenta = 1001015L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.ZON);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_SERVICIO_ZONAL));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_SERVICIO_ZONAL);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.ENT);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.ZON);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void aplicar_costo_de_entrada_cuando_el_viaje_supere_el_maximo_numero_de_trasbordos() throws Exception {
        // given
        long numeroCuenta = 1001016L;
        String infoCuenta = restTemplate.getForEntity(url + "/cuentas/" + numeroCuenta, String.class).getBody();
        Long saldoDisponible = JsonPath.parse(infoCuenta).read("saldoDisponible", Long.class);
        ConsumoEntity input = new ConsumoEntity();
        input.setServicio(Parametro.ServicioConsumo.TRO);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cuentas/" + numeroCuenta + "/consumos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
        assertThatJson(parsedJson).field("['numeroCuenta']").isEqualTo(numeroCuenta);
        assertThatJson(parsedJson).field("['saldoDisponible']").isEqualTo((saldoDisponible - Parametro.COSTO_SERVICIO_TRONCAL));
        assertThatJson(parsedJson).field("['transacciones']").field("['monto']").isEqualTo(Parametro.COSTO_SERVICIO_TRONCAL);
        assertThatJson(parsedJson).field("['transacciones']").field("['modalidad']").isEqualTo(Parametro.ModalidadConsumo.ENT);
        assertThatJson(parsedJson).field("['transacciones']").field("['servicio']").isEqualTo(Parametro.ServicioConsumo.TRO);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }
}
