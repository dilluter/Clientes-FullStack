package io.github.dilluter.exception;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionHandlerControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ExceptionHandlerController exceptionHandler;

    @RestController
    class FakeController {
        @GetMapping("/test-validation")
        public void throwValidation() throws Exception {
        }

        @GetMapping("/test-status")
        public void throwStatus() {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Objeto não encontrado");
        }

        @GetMapping("/test-integrity")
        public void throwIntegrity() {
            throw new DataIntegrityViolationException("Duplicate Key");
        }
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FakeController())
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    public void deveTratarResponseStatusException() throws Exception {
        mockMvc.perform(get("/test-status"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasItem(containsString("Objeto não encontrado"))));
    }

    @Test
    public void deveTratarDataIntegrityViolationException() throws Exception {
        mockMvc.perform(get("/test-integrity"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Já existe um cliente com esse CPF.")));
    }
}