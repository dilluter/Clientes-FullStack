package io.github.dilluter.service;

import io.github.dilluter.dto.servico.request.ServicoPrestadoCreateDTO;
import io.github.dilluter.dto.servico.response.ServicoPrestadoResponseDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.ServicoPrestado;
import io.github.dilluter.model.repository.ServicoPrestadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ServicoPrestadoServiceTest {

    @Mock
    private ServicoPrestadoRepository servicoPrestadoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ServicoPrestadoService servicoPrestadoService;

    @Test
    void deveSalvarServicoPrestado() {
        ServicoPrestadoCreateDTO dto = mock(ServicoPrestadoCreateDTO.class);

        when(dto.getDescricao()).thenReturn("Desenvolvimento de sistema");
        when(dto.getData()).thenReturn("03/05/2026");
        when(dto.getIdCliente()).thenReturn(1);
        when(dto.getValor()).thenReturn(new BigDecimal("1500.00"));

        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        ServicoPrestado servicoSalvo = ServicoPrestado.builder()
                .id(1)
                .descricao("Desenvolvimento de sistema")
                .data(LocalDate.of(2026, 5, 3))
                .cliente(cliente)
                .valor(new BigDecimal("1500.00"))
                .build();

        when(clienteService.buscarEntidadePorId(1)).thenReturn(cliente);
        when(servicoPrestadoRepository.save(any(ServicoPrestado.class))).thenReturn(servicoSalvo);

        ServicoPrestadoResponseDTO resultado = servicoPrestadoService.salvar(dto);

        assertNotNull(resultado);
        verify(clienteService).buscarEntidadePorId(1);
        verify(servicoPrestadoRepository).save(any(ServicoPrestado.class));
    }

    @Test
    void deveLancarBusinessExceptionQuandoDataForInvalida() {
        ServicoPrestadoCreateDTO dto = mock(ServicoPrestadoCreateDTO.class);

        when(dto.getData()).thenReturn("2026-05-03");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> servicoPrestadoService.salvar(dto)
        );

        assertEquals("Data inválida. Use o formato dd/MM/yyyy.", exception.getMessage());

        verify(clienteService, never()).buscarEntidadePorId(any());
        verify(servicoPrestadoRepository, never()).save(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoDataForNula() {
        ServicoPrestadoCreateDTO dto = mock(ServicoPrestadoCreateDTO.class);

        when(dto.getData()).thenReturn(null);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> servicoPrestadoService.salvar(dto)
        );

        assertEquals("Data inválida. Use o formato dd/MM/yyyy.", exception.getMessage());

        verify(clienteService, never()).buscarEntidadePorId(any());
        verify(servicoPrestadoRepository, never()).save(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoDataForAnteriorA2000() {
        ServicoPrestadoCreateDTO dto = mock(ServicoPrestadoCreateDTO.class);

        when(dto.getData()).thenReturn("31/12/1999");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> servicoPrestadoService.salvar(dto)
        );

        assertEquals("A data não pode ser anterior a 01/01/2000.", exception.getMessage());

        verify(clienteService, never()).buscarEntidadePorId(any());
        verify(servicoPrestadoRepository, never()).save(any());
    }

    @Test
    void devePesquisarServicosPrestados() {
        Pageable pageable = PageRequest.of(0, 10);

        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        ServicoPrestado servico = ServicoPrestado.builder()
                .id(1)
                .descricao("Sistema")
                .cliente(cliente)
                .valor(new BigDecimal("1000.00"))
                .data(LocalDate.of(2026, 5, 3))
                .build();

        Page<ServicoPrestado> page = new PageImpl<>(List.of(servico), pageable, 1);

        when(servicoPrestadoRepository.buscarPorNomeClienteEMes("%Igor%", 5, pageable))
                .thenReturn(page);

        Page<ServicoPrestadoResponseDTO> resultado =
                servicoPrestadoService.pesquisar("Igor", 5, pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(servicoPrestadoRepository).buscarPorNomeClienteEMes("%Igor%", 5, pageable);
    }

    @Test
    void devePesquisarServicosPrestadosComNomeNulo() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> page = Page.empty(pageable);

        when(servicoPrestadoRepository.buscarPorNomeClienteEMes("%%", null, pageable))
                .thenReturn(page);

        Page<ServicoPrestadoResponseDTO> resultado =
                servicoPrestadoService.pesquisar(null, null, pageable);

        assertEquals(0, resultado.getTotalElements());

        verify(servicoPrestadoRepository).buscarPorNomeClienteEMes("%%", null, pageable);
    }

    @Test
    void devePesquisarServicosPrestadosRemovendoEspacosDoNome() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> page = Page.empty(pageable);

        when(servicoPrestadoRepository.buscarPorNomeClienteEMes("%Igor%", null, pageable))
                .thenReturn(page);

        Page<ServicoPrestadoResponseDTO> resultado =
                servicoPrestadoService.pesquisar("  Igor  ", null, pageable);

        assertEquals(0, resultado.getTotalElements());

        verify(servicoPrestadoRepository).buscarPorNomeClienteEMes("%Igor%", null, pageable);
    }

    @Test
    void deveLancarBusinessExceptionQuandoMesForMenorQueUm() {
        Pageable pageable = PageRequest.of(0, 10);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> servicoPrestadoService.pesquisar("Igor", 0, pageable)
        );

        assertEquals("Mês inválido. Informe um valor entre 1 e 12.", exception.getMessage());

        verify(servicoPrestadoRepository, never()).buscarPorNomeClienteEMes(any(), any(), any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoMesForMaiorQueDoze() {
        Pageable pageable = PageRequest.of(0, 10);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> servicoPrestadoService.pesquisar("Igor", 13, pageable)
        );

        assertEquals("Mês inválido. Informe um valor entre 1 e 12.", exception.getMessage());

        verify(servicoPrestadoRepository, never()).buscarPorNomeClienteEMes(any(), any(), any());
    }
}