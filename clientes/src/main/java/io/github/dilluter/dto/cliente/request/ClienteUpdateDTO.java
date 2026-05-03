package io.github.dilluter.dto.cliente.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class ClienteUpdateDTO {

    @NotBlank(message = "{campo.nome.obrigatorio}")
    private String nome;

    @NotBlank(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    private String cpf;
}