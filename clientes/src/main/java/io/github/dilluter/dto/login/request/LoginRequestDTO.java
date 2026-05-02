package io.github.dilluter.dto.login.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username obrigatório")
    private String username;

    @NotBlank(message = "Senha obrigatória")
    private String password;
}