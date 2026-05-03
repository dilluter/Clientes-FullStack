package io.github.dilluter.dto.servico.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicoPrestadoCreateDTO {

    @NotBlank(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotBlank(message = "{campo.data.obrigatorio}")
    private String data;

    @NotNull(message = "{campo.cliente.obrigatorio}")
    private Integer idCliente;

    @NotNull(message = "{campo.valor.obrigatorio}")
    private BigDecimal valor;
}