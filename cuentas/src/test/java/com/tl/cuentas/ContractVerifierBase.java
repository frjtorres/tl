package com.tl.cuentas;

import com.tl.cuentas.endpoint.CuentaEndpoint;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CuentasApplication.class)
public abstract class ContractVerifierBase {

    @Autowired
    private CuentaEndpoint cuentaEndpoint;

    @Before
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(cuentaEndpoint);
    }
}
