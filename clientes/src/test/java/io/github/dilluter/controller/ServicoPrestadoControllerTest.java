package io.github.dilluter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dilluter.dto.ServicoPrestadoDTO;
import io.github.dilluter.exception.ExceptionHandlerController;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.ServicoPrestado;
import io.github.dilluter.model.repository.ClienteRepository;
import io.github.dilluter.model.repository.ServicoPrestadoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ServicoPrestadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ServicoPrestadoRepository servicoPrestadoRepository;

    @InjectMocks
    private ServicoPrestadoController servicoPrestadoController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(servicoPrestadoController)
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

    private ServicoPrestadoDTO criarDto(Integer idCliente, String descricao,
                                        String data, BigDecimal valor) {
        ServicoPrestadoDTO servicoPrestadoDTO = new ServicoPrestadoDTO();
        servicoPrestadoDTO.setIdCliente(idCliente);
        servicoPrestadoDTO.setDescricao(descricao);
        servicoPrestadoDTO.setData(data);
        servicoPrestadoDTO.setValor(valor);
        return servicoPrestadoDTO;
    }

    private ServicoPrestado criarServico(Integer id, Cliente cliente,
                                         String descricao, LocalDate data,
                                         BigDecimal valor) {
        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setId(id);
        servicoPrestado.setCliente(cliente);
        servicoPrestado.setDescricao(descricao);
        servicoPrestado.setData(data);
        servicoPrestado.setValor(valor);
        return servicoPrestado;
    }

    @Test
    public void salvar_deveRetornar201EServicoSalvo() throws Exception {
        Cliente cliente = criarCliente(1, "João Silva", "529.982.247-25");

        ServicoPrestadoDTO servicoPrestadoDTO = criarDto(1, "Consultoria", "15/04/2024",
                new BigDecimal("500.00"));

        ServicoPrestado servicoSalvo = criarServico(
                10, cliente, "Consultoria",
                LocalDate.of(2024, 4, 15), new BigDecimal("500.00"));

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(servicoPrestadoRepository.save(any(ServicoPrestado.class)))
                .thenReturn(servicoSalvo);

        mockMvc.perform(post("/api/servicos-prestados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoPrestadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",        is(10)))
                .andExpect(jsonPath("$.descricao", is("Consultoria")));

        verify(clienteRepository,         times(1)).findById(1);
        verify(servicoPrestadoRepository, times(1)).save(any(ServicoPrestado.class));
    }

    @Test
    public void salvar_deveRetornar400QuandoClienteNaoExistente() throws Exception {
        ServicoPrestadoDTO servicoPrestadoDTO = criarDto(99, "Consultoria", "15/04/2024",
                new BigDecimal("500.00"));

        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/servicos-prestados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoPrestadoDTO)))
                .andExpect(status().isBadRequest());

        verify(clienteRepository,         times(1)).findById(99);
        verify(servicoPrestadoRepository, never()).save(any(ServicoPrestado.class));
    }

    @Test
    public void salvar_deveLancarExcecaoQuandoFormatoDeDataInvalido() throws Exception {
        ServicoPrestadoDTO servicoPrestadoDTO = criarDto(1, "Consultoria", "2024-04-15",
                new BigDecimal("500.00"));

        try {
            mockMvc.perform(post("/api/servicos-prestados")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(servicoPrestadoDTO)));
            org.junit.Assert.fail("Esperava ServletException por formato de data inválido");

        } catch (jakarta.servlet.ServletException ex) {
            Throwable cause = ex.getCause();
            org.junit.Assert.assertTrue(
                    "Causa esperada: DateTimeParseException, mas foi: " + cause.getClass(),
                    cause instanceof java.time.format.DateTimeParseException
            );
            org.junit.Assert.assertTrue(
                    "Mensagem deveria conter o texto inválido",
                    cause.getMessage().contains("2024-04-15")
            );
        }
    }

    @Test
    public void pesquisar_deveRetornarListaFiltradaPorNomeEMes() throws Exception {
        Cliente cliente = criarCliente(1, "João Silva", "529.982.247-25");
        ServicoPrestado s1 = criarServico(1, cliente, "Consultoria",
                LocalDate.of(2024, 4, 15), new BigDecimal("500.00"));
        ServicoPrestado s2 = criarServico(2, cliente, "Suporte",
                LocalDate.of(2024, 4, 20), new BigDecimal("200.00"));

        when(servicoPrestadoRepository.findbyNomeClienteAndMes("%João%", 4))
                .thenReturn(Arrays.asList(s1, s2));

        mockMvc.perform(get("/api/servicos-prestados")
                        .param("nome", "João")
                        .param("mes",  "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].descricao", is("Consultoria")))
                .andExpect(jsonPath("$[1].descricao", is("Suporte")));

        verify(servicoPrestadoRepository, times(1))
                .findbyNomeClienteAndMes("%João%", 4);
    }

    @Test
    public void pesquisar_deveRetornarTodosQuandoNomeVazio() throws Exception {
        Cliente cliente = criarCliente(1, "João Silva", "529.982.247-25");
        ServicoPrestado s1 = criarServico(1, cliente, "Consultoria",
                LocalDate.of(2024, 4, 15), new BigDecimal("500.00"));

        when(servicoPrestadoRepository.findbyNomeClienteAndMes("%%", null))
                .thenReturn(Collections.singletonList(s1));

        mockMvc.perform(get("/api/servicos-prestados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(servicoPrestadoRepository, times(1))
                .findbyNomeClienteAndMes("%%", null);
    }

    @Test
    public void pesquisar_deveRetornarListaVaziaQuandoNaoHaResultados() throws Exception {
        when(servicoPrestadoRepository.findbyNomeClienteAndMes(anyString(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/servicos-prestados")
                        .param("nome", "Inexistente")
                        .param("mes",  "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void pesquisar_deveRetornarServicosFiltradosSomentePorMes() throws Exception {
        Cliente cliente = criarCliente(1, "Maria Souza", "071.432.470-75");
        ServicoPrestado s1 = criarServico(3, cliente, "Instalação",
                LocalDate.of(2024, 3, 10), new BigDecimal("350.00"));

        when(servicoPrestadoRepository.findbyNomeClienteAndMes("%%", 3))
                .thenReturn(Collections.singletonList(s1));

        mockMvc.perform(get("/api/servicos-prestados")
                        .param("mes", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].descricao", is("Instalação")));

        verify(servicoPrestadoRepository, times(1))
                .findbyNomeClienteAndMes("%%", 3);
    }
}