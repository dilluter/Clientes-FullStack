package io.github.dilluter.service;

import io.github.dilluter.dto.servico.request.ServicoPrestadoCreateDTO;
import io.github.dilluter.dto.servico.response.ServicoPrestadoResponseDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.ServicoPrestado;
import io.github.dilluter.model.repository.ServicoPrestadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class ServicoPrestadoService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ServicoPrestadoRepository servicoPrestadoRepository;
    private final ClienteService clienteService;

    public ServicoPrestadoResponseDTO salvar(ServicoPrestadoCreateDTO servicoPrestadoCreateDTO) {
        LocalDate data = converterData(servicoPrestadoCreateDTO.getData());

        Cliente cliente = clienteService.buscarEntidadePorId(servicoPrestadoCreateDTO.getIdCliente());

        ServicoPrestado servicoPrestado = ServicoPrestado.builder()
                .descricao(servicoPrestadoCreateDTO.getDescricao())
                .data(data)
                .cliente(cliente)
                .valor(servicoPrestadoCreateDTO.getValor())
                .build();

        ServicoPrestado servicoSalvo = servicoPrestadoRepository.save(servicoPrestado);

        return ServicoPrestadoResponseDTO.fromEntity(servicoSalvo);
    }

    public Page<ServicoPrestadoResponseDTO> pesquisar(String nome, Integer mes, Pageable pageable) {
        validarMes(mes);

        String nomeFiltro = "%" + (nome == null ? "" : nome) + "%";

        return servicoPrestadoRepository
                .buscarPorNomeClienteEMes(nomeFiltro, mes, pageable)
                .map(ServicoPrestadoResponseDTO::fromEntity);
    }

    private LocalDate converterData(String data) {
        try {
            return LocalDate.parse(data, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Data inválida. Use o formato dd/MM/yyyy.");
        }
    }

    private void validarMes(Integer mes) {
        if (mes != null && (mes < 1 || mes > 12)) {
            throw new BusinessException("Mês inválido. Informe um valor entre 1 e 12.");
        }
    }
}