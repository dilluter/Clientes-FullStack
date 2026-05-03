package io.github.dilluter.controller;

import io.github.dilluter.dto.cliente.request.ClienteCreateDTO;
import io.github.dilluter.dto.cliente.request.ClienteUpdateDTO;
import io.github.dilluter.dto.cliente.response.ClienteResponseDTO;
import io.github.dilluter.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void deveListarTodosClientes() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteResponseDTO> pageMock = Page.empty(pageable);

        when(clienteService.listarTodos(pageable)).thenReturn(pageMock);

        Page<ClienteResponseDTO> resultado = clienteController.listarTodos(pageable);

        assertSame(pageMock, resultado);
        verify(clienteService).listarTodos(pageable);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveBuscarClientePorId() {
        Integer id = 1;
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);

        when(clienteService.buscarPorId(id)).thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteController.buscarPorId(id);

        assertSame(responseDTO, resultado);
        verify(clienteService).buscarPorId(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveSalvarCliente() {
        ClienteCreateDTO createDTO = mock(ClienteCreateDTO.class);
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);

        when(clienteService.salvar(createDTO)).thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteController.salvar(createDTO);

        assertSame(responseDTO, resultado);
        verify(clienteService).salvar(createDTO);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveAtualizarCliente() {
        Integer id = 1;
        ClienteUpdateDTO updateDTO = mock(ClienteUpdateDTO.class);
        ClienteResponseDTO responseDTO = mock(ClienteResponseDTO.class);

        when(clienteService.atualizar(id, updateDTO)).thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteController.atualizar(id, updateDTO);

        assertSame(responseDTO, resultado);
        verify(clienteService).atualizar(id, updateDTO);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void deveDeletarCliente() {
        Integer id = 1;

        clienteController.deletar(id);

        verify(clienteService).deletar(id);
        verifyNoMoreInteractions(clienteService);
    }
}
