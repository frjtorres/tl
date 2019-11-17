package com.tl.cuentas.unit;

import com.tl.cuentas.component.TestMapper;
import com.tl.cuentas.model.Cliente;
import com.tl.cuentas.model.Parametro;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ClienteUnit {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestMapper testMapper;

    @Test
    public void omitir_consulta_de_clientes_eliminados() throws Exception {
        // given
        long numeroCliente = 1001L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/clientes")
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        List<Cliente> output = testMapper.mapManyFromJson(mvcResult.getResponse().getContentAsString(), Cliente.class);

        // then
        assertEquals(0, output.stream()
                .filter(a -> a.getNumeroCliente() == numeroCliente)
                .count());
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_consulta_de_cliente_eliminado() throws Exception {
        // given
        long numeroCliente = 1001L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/clientes/" + numeroCliente)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_creacion_de_clientes_con_id_duplicada() throws Exception {
        // given
        Cliente input = new Cliente();
        input.setTipoId(Parametro.TipoId.CC);
        input.setNumeroId("1010234000");
        input.setNombres(RandomStringUtils.randomAlphabetic(10));
        input.setApellidos(RandomStringUtils.randomAlphabetic(10));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_creacion_de_clientes_eliminados_anteriormente() throws Exception {
        // given
        Cliente input = new Cliente();
        input.setTipoId(Parametro.TipoId.CC);
        input.setNumeroId("1010345000");
        input.setNombres(RandomStringUtils.randomAlphabetic(10));
        input.setApellidos(RandomStringUtils.randomAlphabetic(10));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/clientes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Cliente output = testMapper.mapOneFromJson(mvcResult.getResponse().getContentAsString(), Cliente.class);

        // then
        int outputChanges = Comparator.comparing(Cliente::getTipoId)
                .thenComparing(Cliente::getNumeroId)
                .thenComparing(Cliente::getNombres)
                .thenComparing(Cliente::getApellidos)
                .compare(input, output);
        assertEquals(0, outputChanges);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void permitir_solamente_actualizacion_de_nombres_y_apellidos_del_cliente() throws Exception {
        // given
        long numeroCliente = 1004L;
        Cliente input = new Cliente();
        input.setNumeroCliente(Long.parseLong(RandomStringUtils.randomNumeric(4)));
        input.setTipoId(Parametro.TipoId.CE);
        input.setNumeroId(RandomStringUtils.randomNumeric(10));
        input.setEstado(Parametro.EstadoCliente.INA);
        input.setFechaCreacion(new Date(System.currentTimeMillis()));
        input.setFechaModificacion(new Date(System.currentTimeMillis()));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/clientes/" + numeroCliente)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(testMapper.mapToJson(input));
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        Cliente output = testMapper.mapOneFromJson(mvcResult.getResponse().getContentAsString(), Cliente.class);

        // then
        int outputChanges = Comparator.comparing(Cliente::getNumeroCliente)
                .thenComparing(Cliente::getTipoId)
                .thenComparing(Cliente::getNumeroId)
                .thenComparing(Cliente::getEstado)
                .thenComparing(Cliente::getFechaCreacion)
                .thenComparing(Cliente::getFechaModificacion)
                .compare(input, output);
        assertNotEquals(0, outputChanges);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void omitir_eliminacion_de_clientes_con_cuentas_activas() throws Exception {
        // given
        long numeroCliente = 1005L;

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/clientes/" + numeroCliente)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // then
        assertEquals(HttpStatus.CONFLICT.value(), mvcResult.getResponse().getStatus());
    }
}
