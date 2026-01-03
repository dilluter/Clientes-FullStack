package io.github.dilluter.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ServicoPrestadoDTO {

    private String descricao;
    private String data;
    private Integer idCliente;
    private BigDecimal valor;
}
