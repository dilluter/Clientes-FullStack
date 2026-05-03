package io.github.dilluter.dto.servico.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dilluter.model.entity.ServicoPrestado;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ServicoPrestadoResponseDTO {

    private Integer id;
    private String descricao;
    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    private Integer idCliente;
    private String nomeCliente;

    public static ServicoPrestadoResponseDTO fromEntity(ServicoPrestado servico) {
        return ServicoPrestadoResponseDTO.builder()
                .id(servico.getId())
                .descricao(servico.getDescricao())
                .valor(servico.getValor())
                .data(servico.getData())
                .idCliente(servico.getCliente().getId())
                .nomeCliente(servico.getCliente().getNome())
                .build();
    }
}