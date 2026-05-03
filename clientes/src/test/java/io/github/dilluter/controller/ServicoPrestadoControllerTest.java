package io.github.dilluter.controller;

import io.github.dilluter.dto.servico.request.ServicoPrestadoCreateDTO;
import io.github.dilluter.dto.servico.response.ServicoPrestadoResponseDTO;
import io.github.dilluter.service.ServicoPrestadoService;
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
class ServicoPrestadoControllerTest {

    @Mock
    private ServicoPrestadoService servicoPrestadoService;

    @InjectMocks
    private ServicoPrestadoController servicoPrestadoController;

    @Test
    void deveSalvarServicoPrestado() {
        ServicoPrestadoCreateDTO createDTO = mock(ServicoPrestadoCreateDTO.class);
        ServicoPrestadoResponseDTO responseDTO = mock(ServicoPrestadoResponseDTO.class);

        when(servicoPrestadoService.salvar(createDTO)).thenReturn(responseDTO);

        ServicoPrestadoResponseDTO resultado = servicoPrestadoController.salvar(createDTO);

        assertSame(responseDTO, resultado);
        verify(servicoPrestadoService).salvar(createDTO);
        verifyNoMoreInteractions(servicoPrestadoService);
    }

    @Test
    void devePesquisarServicosPrestados() {
        String nome = "Igor";
        Integer mes = 5;
        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestadoResponseDTO> pageMock = Page.empty(pageable);

        when(servicoPrestadoService.pesquisar(nome, mes, pageable)).thenReturn(pageMock);

        Page<ServicoPrestadoResponseDTO> resultado =
                servicoPrestadoController.pesquisar(nome, mes, pageable);

        assertSame(pageMock, resultado);
        verify(servicoPrestadoService).pesquisar(nome, mes, pageable);
        verifyNoMoreInteractions(servicoPrestadoService);
    }

    @Test
    void devePesquisarServicosPrestadosSemFiltroDeNomeEMes() {
        String nome = "";
        Integer mes = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestadoResponseDTO> pageMock = Page.empty(pageable);

        when(servicoPrestadoService.pesquisar(nome, mes, pageable)).thenReturn(pageMock);

        Page<ServicoPrestadoResponseDTO> resultado =
                servicoPrestadoController.pesquisar(nome, mes, pageable);

        assertSame(pageMock, resultado);
        verify(servicoPrestadoService).pesquisar(nome, mes, pageable);
        verifyNoMoreInteractions(servicoPrestadoService);
    }
}
