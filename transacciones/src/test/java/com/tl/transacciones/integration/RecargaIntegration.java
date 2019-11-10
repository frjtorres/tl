package com.tl.transacciones.integration;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tl.transacciones.component.TestMapper;
import com.tl.transacciones.endpoint.TransaccionEndpoint;
import com.tl.transacciones.entity.RecargaEntity;
import com.tl.transacciones.model.Parametro;
import org.junit.Before;
import org.junit.Test;
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

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.tl:cuentas")
public class RecargaIntegration {

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
	private TransaccionEndpoint transaccionEndpoint;

	@Before
	public void setUp() {
		url = dir + ":" + port;
		ReflectionTestUtils.setField(transaccionEndpoint, "cuentasUrl", url);
	}

	@Test
	public void omitir_recargas_en_cuentas_no_activas() throws Exception {
		// given
		long numeroCuenta = 1001008L;
		RecargaEntity input = new RecargaEntity();
		input.setMonto(10_000L);

		// when
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/cuentas/" + numeroCuenta + "/recargas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testMapper.mapToJson(input));
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		// then
		DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		assertThatJson(parsedJson).field("['reasons'][0]").isEqualTo("Las recargas solo estan permitidas para cuentas activas.");
		assertThatJson(parsedJson).field("['status']").isEqualTo(HttpStatus.CONFLICT.value());
		assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void omitir_recargas_cuando_el_monto_no_cubre_los_consumos_pendientes_de_pago() throws Exception {
		// given
		long numeroCuenta = 1001009L;
		RecargaEntity input = new RecargaEntity();
		input.setMonto(3_000L);

		// when
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/cuentas/" + numeroCuenta + "/recargas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testMapper.mapToJson(input));
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		// then
		DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		assertThatJson(parsedJson).field("['reasons'][0]").isEqualTo("La recarga no es suficiente para cubrir los consumos pendientes de pago.");
		assertThatJson(parsedJson).field("['status']").isEqualTo(HttpStatus.CONFLICT.value());
		assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void omitir_recargas_cuando_el_saldo_disponible_supere_el_limite() throws Exception {
		// given
		long numeroCuenta = 1001010L;
		RecargaEntity input = new RecargaEntity();
		input.setMonto(100_000L);

		// when
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/cuentas/" + numeroCuenta + "/recargas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testMapper.mapToJson(input));
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		// then
		DocumentContext parsedJson = JsonPath.parse(mvcResult.getResponse().getContentAsString());
		assertThatJson(parsedJson).field("['reasons'][0]")
				.isEqualTo("La cuenta no puede tener un saldo disponible mayor a " + String.format("%,d", Parametro.MAXIMO_MONTO_CUENTA) + ".");
		assertThatJson(parsedJson).field("['status']").isEqualTo(HttpStatus.CONFLICT.value());
		assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
	}
}
