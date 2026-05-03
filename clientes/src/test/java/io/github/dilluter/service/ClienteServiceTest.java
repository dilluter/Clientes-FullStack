package io.github.dilluter.service;

import io.github.dilluter.dto.cliente.request.ClienteCreateDTO;
import io.github.dilluter.dto.cliente.request.ClienteUpdateDTO;
import io.github.dilluter.dto.cliente.response.ClienteResponseDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.exception.NotFoundException;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveListarTodosClientes() {
        Pageable pageable = PageRequest.of(0, 10);

        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        Page<Cliente> page = new PageImpl<>(List.of(cliente), pageable, 1);

        when(clienteRepository.findAll(pageable)).thenReturn(page);

        Page<ClienteResponseDTO> resultado = clienteService.listarTodos(pageable);

        assertEquals(1, resultado.getTotalElements());
        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        ClienteResponseDTO resultado = clienteService.buscarPorId(1);

        assertNotNull(resultado);
        verify(clienteRepository).findById(1);
    }

    @Test
    void deveLancarNotFoundAoBuscarClienteInexistente() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> clienteService.buscarPorId(99)
        );

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(clienteRepository).findById(99);
    }

    @Test
    void deveSalvarCliente() {
        ClienteCreateDTO dto = mock(ClienteCreateDTO.class);

        when(dto.getNome()).thenReturn("Igor");
        when(dto.getCpf()).thenReturn("52998224725");

        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.empty());

        Cliente clienteSalvo = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        ClienteResponseDTO resultado = clienteService.salvar(dto);

        assertNotNull(resultado);
        verify(clienteRepository).findByCpf("52998224725");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarBusinessExceptionAoSalvarClienteComCpfJaCadastrado() {
        ClienteCreateDTO dto = mock(ClienteCreateDTO.class);

        when(dto.getCpf()).thenReturn("52998224725");

        Cliente clienteExistente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.of(clienteExistente));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> clienteService.salvar(dto)
        );

        assertEquals("Já existe um cliente cadastrado com esse CPF.", exception.getMessage());
        verify(clienteRepository).findByCpf("52998224725");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveAtualizarCliente() {
        ClienteUpdateDTO dto = mock(ClienteUpdateDTO.class);

        when(dto.getNome()).thenReturn("Igor Atualizado");
        when(dto.getCpf()).thenReturn("52998224725");

        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("11144477735")
                .build();

        Cliente clienteAtualizado = Cliente.builder()
                .id(1)
                .nome("Igor Atualizado")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.empty());
        when(clienteRepository.save(cliente)).thenReturn(clienteAtualizado);

        ClienteResponseDTO resultado = clienteService.atualizar(1, dto);

        assertNotNull(resultado);
        assertEquals("Igor Atualizado", cliente.getNome());
        assertEquals("52998224725", cliente.getCpf());

        verify(clienteRepository).findById(1);
        verify(clienteRepository).findByCpf("52998224725");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void devePermitirAtualizarClienteMantendoMesmoCpf() {
        ClienteUpdateDTO dto = mock(ClienteUpdateDTO.class);

        when(dto.getNome()).thenReturn("Igor Atualizado");
        when(dto.getCpf()).thenReturn("52998224725");

        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.atualizar(1, dto);

        assertNotNull(resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarBusinessExceptionAoAtualizarComCpfDeOutroCliente() {
        ClienteUpdateDTO dto = mock(ClienteUpdateDTO.class);

        when(dto.getCpf()).thenReturn("52998224725");

        Cliente clienteAtual = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("11144477735")
                .build();

        Cliente outroCliente = Cliente.builder()
                .id(2)
                .nome("Outro Cliente")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(clienteAtual));
        when(clienteRepository.findByCpf("52998224725")).thenReturn(Optional.of(outroCliente));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> clienteService.atualizar(1, dto)
        );

        assertEquals("Já existe outro cliente cadastrado com esse CPF.", exception.getMessage());

        verify(clienteRepository).findById(1);
        verify(clienteRepository).findByCpf("52998224725");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveDeletarCliente() {
        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        clienteService.deletar(1);

        verify(clienteRepository).findById(1);
        verify(clienteRepository).delete(cliente);
    }

    @Test
    void deveBuscarEntidadePorId() {
        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarEntidadePorId(1);

        assertEquals(cliente, resultado);
        verify(clienteRepository).findById(1);
    }
}