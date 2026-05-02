package io.github.dilluter.dto.cliente.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dilluter.model.entity.Cliente;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ClienteResponseDTO {

    private Integer id;
    private String nome;
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDeCadastro;

    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(cliente.getCpf())
                .dataDeCadastro(cliente.getDataDeCadastro())
                .build();
    }
}