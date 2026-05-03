package io.github.dilluter.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerControllerTest {

    private final ExceptionHandlerController exceptionHandlerController =
            new ExceptionHandlerController();

    @Test
    void deveTratarNotFoundException() {
        NotFoundException exception = new NotFoundException("Cliente não encontrado.");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Cliente não encontrado."));
    }

    @Test
    void deveTratarBusinessException() {
        BusinessException exception = new BusinessException("Erro de regra de negócio.");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleBusinessException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Erro de regra de negócio."));
    }

    @Test
    void deveTratarBadCredentialsException() {
        BadCredentialsException exception =
                new BadCredentialsException("Credenciais inválidas");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleBadCredentialsException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Usuário ou senha inválidos."));
    }

    @Test
    void deveTratarResponseStatusException() {
        ResponseStatusException exception =
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado.");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleResponseStatusException(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Acesso negado."));
    }

    @Test
    void deveTratarDataIntegrityViolationException() {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Erro de integridade");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleDataIntegrityViolation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Violação de integridade no banco de dados."));
    }

    @Test
    void deveTratarExceptionGenerica() {
        Exception exception = new Exception("Erro inesperado");

        ResponseEntity<ApiErrors> response =
                exceptionHandlerController.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getErrors().contains("Erro interno no servidor."));
    }
}