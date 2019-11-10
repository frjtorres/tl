package com.tl.transacciones.integration;

import com.tl.transacciones.component.TestMapper;
import com.tl.transacciones.endpoint.TransaccionEndpoint;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.tl:cuentas")
public class TransaccionIntegration {

	private String url;

	@Value("${com.tl.cuentas.url}")
	private String dir;

	@StubRunnerPort("cuentas")
	private int port;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TransaccionEndpoint transaccionEndpoint;

	@Before
	public void setUp() {
		url = dir + ":" + port;
		ReflectionTestUtils.setField(transaccionEndpoint, "cuentasUrl", url);
	}

	@Test
	public void omitir_consulta_de_transacciones_para_cuenta_eliminada() throws Exception {
		// given
		long numeroCuenta = 1001017L;

		// when
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/cuentas/" + numeroCuenta + "/transacciones")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		// then
		assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
	}

	@Test
	public void omitir_consulta_de_una_transaccion_para_cuenta_eliminada() throws Exception {
		// given
		long numeroTransaccion = 1001002001L;

	    // when
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/transacciones/" + numeroTransaccion)
				.accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		// then
		assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
	}
}
