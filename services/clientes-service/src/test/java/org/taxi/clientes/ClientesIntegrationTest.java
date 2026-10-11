package org.taxi.clientes;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.taxi.clientes.infrastructure.persistence.ClienteJpa;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties="eureka.client.enabled=false") @AutoConfigureMockMvc
class ClientesIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ClienteJpa jpa;
    @BeforeEach void reset() { jpa.deleteAll(); }
    static final String BODY="{\"nombre\":\"Prueba\",\"dni\":\"12345678\",\"telefono\":\"999888777\",\"email\":\"test@example.com\"}";
    @Test void listadoVacioDevuelve200YArregloVacio() throws Exception {
        mvc.perform(get("/api/clientes"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andExpect(content().json("[{}]"));
    }
    @Test void listadoDevuelveLosClientesPersistidosConSusDatos() throws Exception {
        mvc.perform(post("/api/clientes").contentType("application/json").content(BODY))
            .andExpect(status().isCreated());
        mvc.perform(post("/api/clientes").contentType("application/json")
            .content(BODY.replace("Prueba", "Segundo").replace("12345678", "87654321")
                .replace("999888777", "900111222").replace("test@example.com", "segundo@example.com")))
            .andExpect(status().isCreated());
        mvc.perform(get("/api/clientes"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[*].id", org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.greaterThan(0))))
            .andExpect(jsonPath("$[?(@.dni == '12345678')].nombre").value(org.hamcrest.Matchers.contains("Prueba")))
            .andExpect(jsonPath("$[?(@.dni == '12345678')].telefono").value(org.hamcrest.Matchers.contains("999888777")))
            .andExpect(jsonPath("$[?(@.dni == '12345678')].email").value(org.hamcrest.Matchers.contains("test@example.com")))
            .andExpect(jsonPath("$[?(@.dni == '87654321')].nombre").value(org.hamcrest.Matchers.contains("Segundo")))
            .andExpect(jsonPath("$[?(@.dni == '87654321')].telefono").value(org.hamcrest.Matchers.contains("900111222")))
            .andExpect(jsonPath("$[?(@.dni == '87654321')].email").value(org.hamcrest.Matchers.contains("segundo@example.com")));
    }
    @Test void datosPersistidosYDuplicados() throws Exception {
        mvc.perform(post("/api/clientes").contentType("application/json").content(BODY)).andExpect(status().isCreated()).andExpect(jsonPath("$.dni").value("12345678"));
        org.junit.jupiter.api.Assertions.assertEquals(1,jpa.count());
        mvc.perform(post("/api/clientes").contentType("application/json").content(BODY)).andExpect(status().isConflict());
        mvc.perform(get("/api/clientes")).andExpect(jsonPath("$.length()").value(1));
    }
    @Test void rechazaCamposNoDeclarados() throws Exception {
        mvc.perform(post("/api/clientes").contentType("application/json").content(BODY.replace("}",",\"direccion\":\"extra\"}"))).andExpect(status().isBadRequest());
    }
    @Test void rechazaDniInvalido() throws Exception {
        mvc.perform(post("/api/clientes").contentType("application/json").content(BODY.replace("12345678","abc"))).andExpect(status().isBadRequest());
    }
}
