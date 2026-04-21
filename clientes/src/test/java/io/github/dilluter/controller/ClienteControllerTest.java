package io.github.dilluter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dilluter.exception.ExceptionHandlerController;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.repository.ClienteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteController clienteController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(clienteController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    private Cliente criarCliente(Integer id, String nome, String cpf) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        return cliente;
    }

    @Test
    public void obterTodos_deveRetornarListaDeClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(
                criarCliente(1, "João Silva",  "529.982.247-25"),
                criarCliente(2, "Maria Souza", "071.432.470-75")
        );
        when(repository.findAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("João Silva")))
                .andExpect(jsonPath("$[1].nome", is("Maria Souza")));

        verify(repository, times(1)).findAll();
    }

    @Test
    public void obterTodos_deveRetornarListaVaziaQuandoNaoHaClientes() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(repository, times(1)).findAll();
    }

    @Test
    public void buscarPorId_deveRetornarClienteQuandoEncontrado() throws Exception {
        Cliente cliente = criarCliente(1, "João Silva", "529.982.247-25");
        when(repository.findById(1)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",   is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")));

        verify(repository, times(1)).findById(1);
    }

    @Test
    public void buscarPorId_deveRetornar404QuandoClienteNaoEncontrado() throws Exception {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());

        verify(repository, times(1)).findById(99);
    }

    @Test
    public void salvar_deveRetornar201EClienteSalvo() throws Exception {
        Cliente clienteInput = criarCliente(null, "João Silva", "529.982.247-25");
        Cliente clienteSalvo  = criarCliente(1,    "João Silva", "529.982.247-25");

        when(repository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",   is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")));

        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void salvar_deveRetornar400QuandoValidacaoFalha() throws Exception {

        Cliente clienteInvalido = criarCliente(null, "João Silva", null);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());

        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    public void salvar_deveRetornar400QuandoCpfJaExistenteNoBanco() throws Exception {
        Cliente clienteInput = criarCliente(null, "João Silva", "529.982.247-25");

        when(repository.save(any(Cliente.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key value"));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]",
                        is("Já existe um cliente com esse CPF.")));

        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void atualizar_deveRetornar204QuandoClienteAtualizado() throws Exception {
        Cliente clienteExistente  = criarCliente(1, "João Silva",  "529.982.247-25");
        Cliente clienteAtualizado = criarCliente(1, "João Editado","529.982.247-25");

        when(repository.findById(1)).thenReturn(Optional.of(clienteExistente));
        when(repository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isNoContent());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void atualizar_deveRetornar404QuandoClienteNaoExistente() throws Exception {

        Cliente clienteAtualizado = criarCliente(null, "João Editado", "529.982.247-25");

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isNotFound());

        verify(repository, times(1)).findById(99);
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    public void deletarPorId_deveRetornar204QuandoDeletadoComSucesso() throws Exception {
        Cliente cliente = criarCliente(1, "João Silva", "529.982.247-25");
        when(repository.findById(1)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).delete(any(Cliente.class));

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(cliente);
    }

    @Test
    public void deletarPorId_deveRetornar404QuandoClienteNaoEncontrado() throws Exception {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNotFound());

        verify(repository, times(1)).findById(99);
        verify(repository, never()).delete(any(Cliente.class));
    }
}